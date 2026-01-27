package com.shivam.hedera;

import com.hedera.hashgraph.sdk.*;
import io.github.cdimascio.dotenv.Dotenv;

public class ReleaseByAccountId {

    public static void main(String[] args) throws Exception {

        // Load operator (must be contract owner)
        Dotenv env = Dotenv.load();
        AccountId operatorId = AccountId.fromString(env.get("OPERATOR_ID"));
        PrivateKey operatorKey = PrivateKey.fromString(env.get("OPERATOR_KEY"));

        Client client = Client.forTestnet();
        client.setOperator(operatorId, operatorKey);

        System.out.println("Operator Hedera ID : " + operatorId);
        System.out.println("Operator EVM Addr  : 0x" + operatorId.toSolidityAddress());

        // ----------------- UPDATE THESE -----------------
        String contractIdStr = "0.0.7719192";     // Your deployed escrow contract
        String tokenIdStr    = "0.0.6460709";     // Token ID
        String recipientStr  = "0.0.6438872";     // Receiver Hedera Account
        long amount          = 100L;              // Atomic 
        // ------------------------------------------------

        ContractId contractId = ContractId.fromString(contractIdStr);
        TokenId tokenId = TokenId.fromString(tokenIdStr);

        // Convert recipient Hedera account → mirror EVM address
        String recipientEvm = "0x" + AccountId.fromString(recipientStr).toSolidityAddress();

        System.out.println("--------------------------------------------------");
        System.out.println("Contract ID        : " + contractId);
        System.out.println("Contract EVM       : 0x" + contractId.toSolidityAddress());
        System.out.println("Token ID           : " + tokenId);
        System.out.println("Token EVM          : 0x" + tokenId.toSolidityAddress());
        System.out.println("Recipient Account  : " + recipientStr);
        System.out.println("Recipient EVM      : " + recipientEvm);
        System.out.println("Amount (atomic)    : " + amount);
        System.out.println("--------------------------------------------------");

        // Call releaseToAddress(token, recipient, amount)
        ContractExecuteTransaction tx = new ContractExecuteTransaction()
                .setContractId(contractId)
                .setGas(1_000_000)
                .setFunction(
                        "releaseToAddress",
                        new ContractFunctionParameters()
                                .addAddress(tokenId.toSolidityAddress())
                                .addAddress(recipientEvm)
                                .addInt64(amount)
                );

        TransactionResponse resp = tx.execute(client);
        TransactionReceipt receipt = resp.getReceipt(client);

        System.out.println("Transaction ID : " + resp.transactionId);
        System.out.println("Status         : " + receipt.status);

        if (receipt.status == Status.SUCCESS) {
            System.out.println("Tokens released successfully!");
        } else {
            System.out.println("Release failed: " + receipt.status);
        }
    }
}
