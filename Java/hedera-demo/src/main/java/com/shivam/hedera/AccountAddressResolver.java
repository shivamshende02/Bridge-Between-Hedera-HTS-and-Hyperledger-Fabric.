package com.shivam.hedera;

import com.hedera.hashgraph.sdk.*;
import io.github.cdimascio.dotenv.Dotenv;

public class AccountAddressResolver {
    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();
        AccountId operatorId = AccountId.fromString(dotenv.get("OPERATOR_ID"));
        PrivateKey operatorKey = PrivateKey.fromString(dotenv.get("OPERATOR_KEY"));

        Client client = Client.forTestnet();
        client.setOperator(operatorId, operatorKey);

        // Friend’s Hedera account
        AccountId friendId = AccountId.fromString("0.0.6438872");

        // Get AccountInfo
        AccountInfo info = new AccountInfoQuery()
                .setAccountId(friendId)
                .execute(client);

        // ✅ Safe solidity address (mirror mapping)
        String mirrorAddr = friendId.toSolidityAddress();

        // ✅ Contract account ID (sometimes used as EVM address if account is contract-based)
        String contractAlias = info.contractAccountId;

        System.out.println("Mirror mapping address (use this for transfers): 0x" + mirrorAddr);
        System.out.println("Contract/EVM alias (if set): " + contractAlias);
    }
}
