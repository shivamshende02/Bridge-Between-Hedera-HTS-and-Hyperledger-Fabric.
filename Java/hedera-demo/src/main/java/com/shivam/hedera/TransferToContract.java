package com.shivam.hedera;

import com.hedera.hashgraph.sdk.*;
import io.github.cdimascio.dotenv.Dotenv;

public class TransferToContract {
    public static void main(String[] args) throws Exception {
        // Connect to Hedera Testnet
        Client client = Client.forTestnet();

        // Load operator credentials from .env file
        Dotenv dotenv = Dotenv.load();
        PrivateKey operatorKey = PrivateKey.fromString(dotenv.get("OPERATOR_KEY"));
        AccountId operatorId = AccountId.fromString(dotenv.get("OPERATOR_ID"));
        client.setOperator(operatorId, operatorKey);

        // Define token (fungible) and destination contract
        TokenId tokenId = TokenId.fromString("0.0.6460709");
        AccountId contractAccountId = AccountId.fromString("0.0.7278925");

        // Build the transfer transaction (sending 100 tokens to contract)
        TransferTransaction tx = new TransferTransaction()
            .addTokenTransfer(tokenId, client.getOperatorAccountId(), -100)
            .addTokenTransfer(tokenId, contractAccountId, 100)
            .freezeWith(client)
            .sign(operatorKey);

        // Submit transaction and get both the hash AND final receipt/status
        TransactionResponse response = tx.execute(client);
        String transactionHash = bytesToHex(response.transactionHash);
        TransactionReceipt receipt = response.getReceipt(client);

        System.out.println("✅ Transaction submitted!");
        System.out.println("Transaction hash: 0x" + transactionHash);
        System.out.println("Token transfer status: " + receipt.status);

        if ("SUCCESS".equalsIgnoreCase(receipt.status.toString())) {
            System.out.println("Transfer succeeded.");
        } else {
            System.out.println("Transfer failed or partial: " + receipt.status);
        }
    }

    // Helper to display transaction hash as hex
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
