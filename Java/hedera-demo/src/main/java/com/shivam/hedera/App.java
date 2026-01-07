package com.shivam.hedera;

import io.github.cdimascio.dotenv.Dotenv;
import com.hedera.hashgraph.sdk.*;

import java.util.concurrent.TimeoutException;

public class App {
    public static void main(String[] args) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {
        // Load from .env
        Dotenv dotenv = Dotenv.load();
        String operatorId = dotenv.get("OPERATOR_ID");
        String operatorKey = dotenv.get("OPERATOR_KEY");

        if (operatorId == null || operatorKey == null) {
            throw new IllegalArgumentException("Please set OPERATOR_ID and OPERATOR_KEY in .env");
        }

        Client client = Client.forTestnet();
        client.setOperator(AccountId.fromString(operatorId), PrivateKey.fromString(operatorKey));

        AccountBalance balance = new AccountBalanceQuery()
            .setAccountId(AccountId.fromString(operatorId))
            .execute(client);

        System.out.println("My balance: " + balance.hbars);
    }
}
