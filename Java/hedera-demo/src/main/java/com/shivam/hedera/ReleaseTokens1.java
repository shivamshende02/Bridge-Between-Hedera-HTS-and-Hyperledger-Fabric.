package com.shivam.hedera;

import com.hedera.hashgraph.sdk.*;
import io.github.cdimascio.dotenv.Dotenv;

public class ReleaseTokens1 {
    public static void main(String[] args) throws Exception {
        // Load operator (can be anyone; your contract allows any caller)
        Dotenv dotenv = Dotenv.load();
        AccountId operatorId = AccountId.fromString(dotenv.get("OPERATOR_ID"));
        PrivateKey operatorKey = PrivateKey.fromString(dotenv.get("OPERATOR_KEY"));

        Client client = Client.forTestnet();
        client.setOperator(operatorId, operatorKey);

        // IDs
        ContractId contractId = ContractId.fromString("0.0.6936536");   // your escrow
        TokenId tokenId     = TokenId.fromString("0.0.6460709");        // your FT
        AccountId recipient = AccountId.fromString("0.0.6207371");      // who should receive

        // Correct Solidity/EVM address conversions
        String tokenSolAddr     = tokenId.toSolidityAddress();          // 20-byte address for token
        String recipientSolAddr = recipient.toSolidityAddress();        // 20-byte mirror address (shard|realm|num)

        long amount = 100; // NOTE: in the token's smallest unit. Ensure this matches your token's decimals.

        // Build and execute: releaseTokens(address token, address recipient, int64 amount)
        ContractExecuteTransaction tx = new ContractExecuteTransaction()
            .setContractId(contractId)
            .setGas(2_000_000)
            .setFunction(
                "releaseTokens",
                new ContractFunctionParameters()
                    .addAddress(tokenSolAddr)
                    .addAddress(recipientSolAddr)
                    .addInt64(amount)                 // <-- match solidity int64
            );

        TransactionResponse resp = tx.execute(client);
        TransactionReceipt receipt = resp.getReceipt(client);

        System.out.println("Release status: " + receipt.status);
    }
}
