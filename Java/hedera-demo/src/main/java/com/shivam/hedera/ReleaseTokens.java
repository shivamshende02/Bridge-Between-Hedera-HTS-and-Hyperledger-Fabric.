package com.shivam.hedera;

import com.hedera.hashgraph.sdk.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Duration;

public class ReleaseTokens {

    // --- Utilities -----------------------------------------------------------

    // Parse "shard.realm.num" -> long[]{shard, realm, num}
    private static long[] parseAccountId(String s) {
        String[] p = s.trim().split("\\.");
        if (p.length != 3) throw new IllegalArgumentException("Bad AccountId: " + s);
        return new long[]{ Long.parseLong(p[0]), Long.parseLong(p[1]), Long.parseLong(p[2]) };
    }

    // Build the 20-byte mirror (Solidity) address: shard(4) | realm(8) | num(8), big-endian → "0x…"
    private static String toMirrorSolidityAddress(String accountIdDot) {
        long[] srn = parseAccountId(accountIdDot);
        long shard = srn[0], realm = srn[1], num = srn[2];

        ByteBuffer buf = ByteBuffer.allocate(20);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putInt((int) shard);   // Hedera shards fit in 32-bit today
        buf.putLong(realm);
        buf.putLong(num);

        byte[] bytes = buf.array();
        StringBuilder sb = new StringBuilder(2 + bytes.length * 2);
        sb.append("0x");
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    // ------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();
        AccountId operatorId = AccountId.fromString(dotenv.get("OPERATOR_ID"));
        PrivateKey operatorKey = PrivateKey.fromString(dotenv.get("OPERATOR_KEY"));

        // Client with more generous timeouts/retries
        Client client = Client.forTestnet();
        client.setOperator(operatorId, operatorKey);
        client.setRequestTimeout(Duration.ofSeconds(60));
        client.setMaxAttempts(10);
        client.setMinBackoff(Duration.ofMillis(500));
        client.setMaxBackoff(Duration.ofSeconds(3));

        // Quick connectivity probe
        var bal = new AccountBalanceQuery()
                .setAccountId(operatorId)
                .execute(client);
        System.out.println("Operator HBAR: " + bal.hbars);

        // --- Params you change ------------------------------------------------
        ContractId contractId = ContractId.fromString("0.0.7278925");  // escrow contract
        TokenId tokenId       = TokenId.fromString("0.0.6460709");     // token
        long amount           = 100L;                                  // int64 in Solidity (smallest unit)
        String recipientDot   = "0.0.6438872";                         // Hedera AccountId (portal ED25519)
        // ---------------------------------------------------------------------

        // Compute addresses correctly
        String tokenSolAddr     = tokenId.toSolidityAddress();              // token as EVM address
        String recipientMirror  = toMirrorSolidityAddress(recipientDot);    // recipient mirror 0x (NOT alias)

        System.out.printf("Releasing %d of %s from %s to %s (mirror of %s)%n",
                amount, tokenId, contractId, recipientMirror, recipientDot);

        // Build the contract call: releaseTokens(address token, address recipient, int64 amount)
        ContractExecuteTransaction releaseTx = new ContractExecuteTransaction()
                .setContractId(contractId)
                .setGas(2_500_000)
                .setFunction(
                        "releaseTokens",
                        new ContractFunctionParameters()
                                .addAddress(tokenSolAddr)
                                .addAddress(recipientMirror)
                                .addInt64(amount)            // match Solidity int64
                )
                .setTransactionValidDuration(Duration.ofMinutes(2))
                .setGrpcDeadline(Duration.ofSeconds(20));

        // Freeze & sign, then submit
        releaseTx.freezeWith(client).sign(operatorKey);

        TransactionResponse resp = releaseTx.execute(client);
        System.out.println("Submitted. TxId: " + resp.transactionId);

        TransactionReceipt receipt = resp.getReceipt(client);
        System.out.println("Release status: " + receipt.status);
    }
}
