package com.shivam.hedera;

import com.hedera.hashgraph.sdk.*;

import io.github.cdimascio.dotenv.Dotenv;

public class AccountInfoExample {

    public static void main(String[] args) throws Exception {

    	Dotenv dotenv = Dotenv.load();
        AccountId operatorId = AccountId.fromString(dotenv.get("OPERATOR_ID"));
        PrivateKey operatorKey = PrivateKey.fromString(dotenv.get("OPERATOR_KEY"));

        Client client = Client.forTestnet();
        client.setOperator(operatorId, operatorKey);

        String accountIdString = "0.0.6207371";
        AccountId accountId = AccountId.fromString(accountIdString);

        AccountInfo info = new AccountInfoQuery()
                .setAccountId(accountId)
                .execute(client);

        System.out.println("Mirror Address: " + accountId.toSolidityAddress());
        System.out.println("Alias Key: " + info.aliasKey);
        //System.out.println("Ethereum Address: " + info.ethereumAddress);
    }
}