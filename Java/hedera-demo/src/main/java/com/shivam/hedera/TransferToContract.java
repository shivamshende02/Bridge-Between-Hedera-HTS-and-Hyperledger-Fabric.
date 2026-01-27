package com.shivam.hedera;

import com.hedera.hashgraph.sdk.*;
import io.github.cdimascio.dotenv.Dotenv;

public class TransferToContract {
    public static void main(String[] args) throws Exception {
        
        // Setup client
        Dotenv dotenv = Dotenv.load();
        PrivateKey operatorKey = PrivateKey.fromString(dotenv.get("OPERATOR_KEY"));
        AccountId operatorId = AccountId.fromString(dotenv.get("OPERATOR_ID"));
        
        Client client = Client.forTestnet();
        client.setOperator(operatorId, operatorKey);

        // Configuration
        TokenId tokenId = TokenId.fromString("0.0.6460709");
        ContractId contractId = ContractId.fromString(dotenv.get("CONTRACT_ID"));
        AccountId contractAccountId = AccountId.fromString(contractId.toString());
        long amount = 100;

        System.out.println("═══════════════════════════════════════════");
        System.out.println("🔄 STEP 1: Transferring tokens to contract");
        System.out.println("═══════════════════════════════════════════");
        
        // Step 1: Transfer tokens to contract
        TransferTransaction transferTx = new TransferTransaction()
            .addTokenTransfer(tokenId, operatorId, -amount)
            .addTokenTransfer(tokenId, contractAccountId, amount)
            .freezeWith(client)
            .sign(operatorKey);

        TransactionResponse transferResponse = transferTx.execute(client);
        TransactionReceipt transferReceipt = transferResponse.getReceipt(client);

        System.out.println("✅ Transfer Status: " + transferReceipt.status);
        System.out.println("Transaction ID: " + transferResponse.transactionId);
        
        // Wait a moment for network propagation
        System.out.println("\n⏳ Waiting 2 seconds...\n");
        Thread.sleep(2000);

        System.out.println("═══════════════════════════════════════════");
        System.out.println("🔒 STEP 2: Calling lockTokens to emit event");
        System.out.println("═══════════════════════════════════════════");
        
        // Step 2: Call lockTokens to emit the event
        ContractExecuteTransaction lockTx = new ContractExecuteTransaction()
            .setContractId(contractId)
            .setGas(300000)
            .setFunction(
                "lockTokens",
                new ContractFunctionParameters()
                    .addAddress(tokenId.toSolidityAddress())
                    .addInt64(amount)
            )
            .freezeWith(client)
            .sign(operatorKey);

        TransactionResponse lockResponse = lockTx.execute(client);
        TransactionReceipt lockReceipt = lockResponse.getReceipt(client);

        System.out.println("✅ Lock Status: " + lockReceipt.status);
        System.out.println("Transaction ID: " + lockResponse.transactionId);
        
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("✅ COMPLETE!");
        System.out.println("═══════════════════════════════════════════");
        System.out.println("Token:    0.0.6460709");
        System.out.println("Amount:   " + amount);
        System.out.println("Contract: " + contractId);
        System.out.println("\n⏳ Wait 5-10 seconds for Mirror Node to index...");
        System.out.println("👀 Check your listener terminal for the event!");
        
        client.close();
    }
}
