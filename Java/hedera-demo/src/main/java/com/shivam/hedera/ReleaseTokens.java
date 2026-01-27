package com.shivam.hedera;

import com.hedera.hashgraph.sdk.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.Duration;

public class ReleaseTokens {

    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();
        AccountId operatorId = AccountId.fromString(dotenv.get("OPERATOR_ID"));
        PrivateKey operatorKey = PrivateKey.fromString(dotenv.get("OPERATOR_KEY"));

        Client client = Client.forTestnet();
        client.setOperator(operatorId, operatorKey);
        client.setRequestTimeout(Duration.ofSeconds(60));

        // Check connectivity
        var bal = new AccountBalanceQuery()
                .setAccountId(operatorId)
                .execute(client);
        System.out.println("Operator HBAR: " + bal.hbars);

        // --- Your parameters ---
        ContractId contractId = ContractId.fromString("0.0.7278925");
        TokenId tokenId = TokenId.fromString("0.0.6460709");
        long amount = 100L;  // Amount in smallest unit

        String tokenSolAddr = tokenId.toSolidityAddress();

        System.out.printf("Releasing %d tokens from contract %s to owner (operator)%n",
                amount, contractId);

        // ✅ CORRECT: Call releaseToOwner(address token, int64 amount)
        ContractExecuteTransaction releaseTx = new ContractExecuteTransaction()
                .setContractId(contractId)
                .setGas(800_000)  // Sufficient for HTS transfer
                .setFunction(
                        "releaseToOwner",  // ✅ Correct function name
                        new ContractFunctionParameters()
                                .addAddress(tokenSolAddr)  // Token address
                                .addInt64(amount)          // Amount (int64)
                )
                .setTransactionValidDuration(Duration.ofMinutes(2))
                .freezeWith(client)
                .sign(operatorKey);

        TransactionResponse resp = releaseTx.execute(client);
        System.out.println("✅ Submitted. TxId: " + resp.transactionId);

        TransactionReceipt receipt = resp.getReceipt(client);
        System.out.println("Release status: " + receipt.status);

        if (receipt.status == Status.SUCCESS) {
            System.out.println("✅ Tokens released to owner successfully!");
        } else {
            System.out.println("❌ Release failed: " + receipt.status);
        }
    }
}
