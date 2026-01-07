package com.shivam.hedera;

import com.hedera.hashgraph.sdk.*;
import io.github.cdimascio.dotenv.Dotenv;

public class ReleaseByAccountId {

    // Parse "0.0.6438872" → {shard=0, realm=0, num=6438872}
    private static long[] parseAccountId(String accountId) {
        // very small, safe parser for "shard.realm.num"
        String trimmed = accountId.trim();
        if (!trimmed.contains(".")) {
            throw new IllegalArgumentException("AccountId must be in shard.realm.num format, got: " + accountId);
        }
        String[] parts = trimmed.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("AccountId must have exactly 3 parts: " + accountId);
        }
        long shard = Long.parseLong(parts[0]);
        long realm = Long.parseLong(parts[1]);
        long num   = Long.parseLong(parts[2]);
        return new long[]{shard, realm, num};
    }

    public static void main(String[] args) throws Exception {
        // Load operator (payer) from .env
        Dotenv env = Dotenv.load();
        AccountId operatorId = AccountId.fromString(env.get("OPERATOR_ID"));
        PrivateKey operatorKey = PrivateKey.fromString(env.get("OPERATOR_KEY"));

        Client client = Client.forTestnet();
        client.setOperator(operatorId, operatorKey);

        // --- UPDATE THESE THREE VALUES ---
        String contractIdStr = "0.0.6791530";     // escrow contract
        String tokenIdStr    = "0.0.6460709";     // FT to release
        String recipientStr  = "0.0.6438872";     // who should receive
        long   amount        = 100L;              // int64 in Solidity (token's smallest unit)
        // ---------------------------------

        ContractId contractId = ContractId.fromString(contractIdStr);
        TokenId tokenId       = TokenId.fromString(tokenIdStr);

        // convert token to Solidity address (this part IS safe)
        String tokenSolAddr = tokenId.toSolidityAddress();

        // parse recipient AccountId into shard/realm/num
        long[] s_r_n = parseAccountId(recipientStr);
        long shard = s_r_n[0];
        long realm = s_r_n[1];
        long num   = s_r_n[2];

        System.out.printf("Releasing %d of %s from %s to AccountId(%d.%d.%d)%n",
                amount, tokenIdStr, contractIdStr, shard, realm, num);

        // call: releaseByAccountId(address token, uint32 shard, uint64 realm, uint64 num, int64 amount)
        ContractExecuteTransaction tx = new ContractExecuteTransaction()
                .setContractId(contractId)
                .setGas(2_000_000) // bump if you see INSUFFICIENT_GAS
                .setFunction(
                        "releaseByAccountId",
                        new ContractFunctionParameters()
                                .addAddress(tokenSolAddr)   // address token
                                .addUint32((int) shard)     // shard (fits in uint32 for Hedera)
                                .addUint64(realm)           // realm
                                .addUint64(num)             // num
                                .addInt64(amount)           // int64 amount (matches Solidity)
                );

        TransactionResponse resp = tx.execute(client);
        TransactionReceipt receipt = resp.getReceipt(client);
        System.out.println("Release status: " + receipt.status);

        // Optional: verify post-state (contract and recipient balances)
        // AccountBalance contractBal = new AccountBalanceQuery()
        //         .setAccountId(contractId.toAccountId())
        //         .execute(client);
        // Long contractTokenBal = contractBal.tokens.get(tokenId);
        // System.out.println("Contract token balance after release: " + (contractTokenBal == null ? 0 : contractTokenBal));
    }
}
