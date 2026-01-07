package com.shivam.hedera;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HederaMirrorQuery {

    // Mirror node base URL (use testnet here)
    private static final String MIRROR_NODE = "https://testnet.mirrornode.hedera.com/api/v1";

    // Contract and token details
    private static final String CONTRACT_ID = "0.0.6791530";   // escrow contract
    private static final String TOKEN_ID = "0.0.6460709";      // token

    public static void main(String[] args) {
        try {
            // 1️⃣ Check balance of the escrow contract
            String balanceUrl = MIRROR_NODE + "/accounts/" + CONTRACT_ID;
            System.out.println("🔍 Checking balance for contract: " + CONTRACT_ID);
            String balanceResponse = get(balanceUrl);
            System.out.println("Balance Response: \n" + balanceResponse);

            // 2️⃣ Check token transfer transactions for the escrow contract
            String txUrl = MIRROR_NODE + "/transactions?account.id=" + CONTRACT_ID + "&transactionType=CRYPTOTRANSFER&order=desc&limit=5";
            System.out.println("\n🔍 Checking last 5 transactions for contract: " + CONTRACT_ID);
            String txResponse = get(txUrl);
            System.out.println("Transaction Response: \n" + txResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method: send GET request
    private static String get(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine).append("\n");
        }
        in.close();
        conn.disconnect();

        return content.toString();
    }
}
