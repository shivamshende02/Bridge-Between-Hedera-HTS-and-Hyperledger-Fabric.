package com.shivam.hedera;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shivam.hedera.bridge.HLFBridgeClient;
import com.shivam.hedera.model.BridgePayload;
import io.github.cdimascio.dotenv.Dotenv;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Int64;
import org.web3j.abi.datatypes.generated.Uint256;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

public class HederaListener {

    private static final String MIRROR_NODE_URL = "https://testnet.mirrornode.hedera.com/api/v1";
    private static String contractEvmAddress;
    private static String contractId;
    private static final String TOKENS_LOCKED_SIGNATURE;
    private static HLFBridgeClient hlfClient;
    
    static {
        Event tokensLockedEvent = new Event("TokensLocked",
            Arrays.asList(
                new TypeReference<Address>(true) {},
                new TypeReference<Address>(true) {},
                new TypeReference<Int64>() {},
                new TypeReference<Uint256>() {}
            )
        );
        TOKENS_LOCKED_SIGNATURE = EventEncoder.encode(tokensLockedEvent);
        System.out.println("TokensLocked Event Signature: " + TOKENS_LOCKED_SIGNATURE);
    }

    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();
        contractId = dotenv.get("CONTRACT_ID");
        String hlfEndpoint = dotenv.get("HLF_API_ENDPOINT");
        
        // Initialize HLF client
        hlfClient = new HLFBridgeClient(hlfEndpoint);
        
        contractEvmAddress = contractIdToEvmAddress(contractId);
        System.out.println("Listening for events from contract: " + contractId);
        System.out.println("EVM Address: " + contractEvmAddress);
        System.out.println("HLF Bridge Endpoint: " + hlfEndpoint);
        System.out.println("Polling Mirror Node every 5 seconds...\n");
        
        pollMirrorNode();
    }

    private static void pollMirrorNode() throws InterruptedException {
        String lastTimestamp = null;
        HttpClient client = HttpClient.newHttpClient();
        int pollCount = 0;
        
        while (true) {
            try {
                pollCount++;
                String url = buildMirrorNodeUrl(lastTimestamp);
                
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
                
                HttpResponse<String> response = client.send(request, 
                    HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() == 200) {
                    String newTimestamp = processEvents(response.body(), lastTimestamp);
                    if (newTimestamp != null && !newTimestamp.equals(lastTimestamp)) {
                        lastTimestamp = newTimestamp;
                    } else if (pollCount % 12 == 0) {
                        System.out.println("⏱️  Still listening... (poll #" + pollCount + ")");
                    }
                } else {
                    System.err.println("❌ Mirror Node error: " + response.statusCode());
                    System.err.println("Response: " + response.body());
                }
                
            } catch (IOException | InterruptedException e) {
                System.err.println("Error: " + e.getMessage());
            }
            
            Thread.sleep(5000);
        }
    }

    private static String buildMirrorNodeUrl(String fromTimestamp) {
        StringBuilder url = new StringBuilder(MIRROR_NODE_URL);
        url.append("/contracts/").append(contractEvmAddress).append("/results/logs");
        url.append("?order=asc&limit=10");
        
        long now = System.currentTimeMillis() / 1000;
        
        if (fromTimestamp != null) {
            url.append("&timestamp=gt:").append(fromTimestamp);
            url.append("&timestamp=lte:").append(now);
        } else {
            long oneHourAgo = now - 3600;
            url.append("&timestamp=gte:").append(oneHourAgo);
            url.append("&timestamp=lte:").append(now);
        }
        
        url.append("&topic0=").append(TOKENS_LOCKED_SIGNATURE);
        return url.toString();
    }

    private static String processEvents(String jsonResponse, String lastTimestamp) {
        JsonObject json = JsonParser.parseString(jsonResponse).getAsJsonObject();
        JsonArray logs = json.getAsJsonArray("logs");
        
        if (logs == null || logs.size() == 0) {
            return lastTimestamp;
        }
        
        System.out.println("🔔 Found " + logs.size() + " new TokensLocked event(s)!\n");
        
        String newestTimestamp = lastTimestamp;
        
        for (int i = 0; i < logs.size(); i++) {
            JsonObject log = logs.get(i).getAsJsonObject();
            String timestamp = log.get("timestamp").getAsString();
            JsonArray topics = log.getAsJsonArray("topics");
            String data = log.get("data").getAsString();
            
            // Decode event
            TokenLockEvent event = decodeTokensLockedEvent(topics, data, timestamp);
            printEventDetails(event);
            
            // Create and send payload to HLF
            BridgePayload payload = createBridgePayload(event);
            System.out.println("\n📤 Bridge Payload:");
            System.out.println(payload.toJson());
            
            boolean success = hlfClient.sendLockEvent(payload);
            if (success) {
                System.out.println("✅ Successfully sent to HLF!\n");
            } else {
                System.err.println("❌ Failed to send to HLF\n");
            }
            
            newestTimestamp = timestamp;
        }
        
        return newestTimestamp;
    }

    private static BridgePayload createBridgePayload(TokenLockEvent event) {
        BridgePayload payload = new BridgePayload();
        
        // Contract info
        payload.setContractId(contractId);
        payload.setContractEvmAddress(contractEvmAddress);
        
        // Token info
        payload.setTokenEvmAddress(event.token);
        payload.setTokenId(evmAddressToTokenId(event.token));
        
        // Sender info
        payload.setSenderEvmAddress(event.sender);
        payload.setSenderAccountId(evmAddressToAccountId(event.sender));
        
        // Amount
        payload.setAmount(event.amount);
        
        // Timestamps
        payload.setConsensusTimestamp(event.timestamp);
        payload.setBlockTimestamp(String.valueOf(event.blockTimestamp));
        
        // Event signature (proof this is TokensLocked)
        payload.setEventSignature(TOKENS_LOCKED_SIGNATURE);
        
        // Mirror Node verification URL
        payload.setMirrorNodeEventUrl(
            MIRROR_NODE_URL + "/contracts/" + contractEvmAddress + 
            "/results/logs?timestamp=" + event.timestamp
        );
        
        return payload;
    }

    private static String evmAddressToTokenId(String evmAddress) {
        if (evmAddress == null || evmAddress.isEmpty()) {
            return "0.0.0";
        }
        
        // Remove 0x prefix
        if (evmAddress.startsWith("0x")) {
            evmAddress = evmAddress.substring(2);
        }
        
        // Pad to 40 characters if needed
        while (evmAddress.length() < 40) {
            evmAddress = "0" + evmAddress;
        }
        
        // Take last 16 hex chars (8 bytes) for the entity number
        try {
            String entityHex = evmAddress.substring(24); // Last 16 chars
            long tokenNum = Long.parseUnsignedLong(entityHex, 16);
            return "0.0." + tokenNum;
        } catch (Exception e) {
            System.err.println("Error converting token address: " + evmAddress);
            e.printStackTrace();
            return "0.0.0";
        }
    }

    private static String evmAddressToAccountId(String evmAddress) {
        if (evmAddress == null || evmAddress.isEmpty()) {
            return "0.0.0";
        }
        
        // Remove 0x prefix
        if (evmAddress.startsWith("0x")) {
            evmAddress = evmAddress.substring(2);
        }
        
        // Pad to 40 characters if needed
        while (evmAddress.length() < 40) {
            evmAddress = "0" + evmAddress;
        }
        
        // Take last 16 hex chars (8 bytes) for the entity number
        try {
            String entityHex = evmAddress.substring(24); // Last 16 chars
            long accountNum = Long.parseUnsignedLong(entityHex, 16);
            return "0.0." + accountNum;
        } catch (Exception e) {
            System.err.println("Error converting account address: " + evmAddress);
            e.printStackTrace();
            return "0.0.0";
        }
    }


    private static TokenLockEvent decodeTokensLockedEvent(JsonArray topics, String data, String timestamp) {
        TokenLockEvent event = new TokenLockEvent();
        event.timestamp = timestamp;
        
        if (topics != null && topics.size() >= 3) {
            event.token = topicToAddress(topics.get(1).getAsString());
            event.sender = topicToAddress(topics.get(2).getAsString());
        }
        
        if (data != null && !data.equals("0x")) {
            try {
                String hex = data.startsWith("0x") ? data.substring(2) : data;
                if (hex.length() >= 128) {
                    event.amount = Long.parseUnsignedLong(hex.substring(48, 64), 16);
                    event.blockTimestamp = Long.parseUnsignedLong(hex.substring(112, 128), 16);
                }
            } catch (Exception e) {
                System.err.println("Decode error: " + e.getMessage());
            }
        }
        
        return event;
    }

    private static String topicToAddress(String topic) {
        if (topic == null || topic.isEmpty()) {
            return "0x0000000000000000000000000000000000000000";
        }
        
        // Remove 0x prefix if present
        if (topic.startsWith("0x")) {
            topic = topic.substring(2);
        }
        
        // Topics are 32 bytes (64 hex chars), address is last 20 bytes (40 hex chars)
        if (topic.length() == 64) {
            return "0x" + topic.substring(24); // Last 40 chars
        } else if (topic.length() == 40) {
            return "0x" + topic; // Already correct length
        } else {
            // Pad or truncate as needed
            while (topic.length() < 40) {
                topic = "0" + topic;
            }
            if (topic.length() > 40) {
                topic = topic.substring(topic.length() - 40);
            }
            return "0x" + topic;
        }
    }


    private static String contractIdToEvmAddress(String contractId) {
        String[] parts = contractId.split("\\.");
        long num = Long.parseLong(parts[2]);
        return String.format("0x%040x", num);
    }

    private static void printEventDetails(TokenLockEvent event) {
        System.out.println("═══════════════════════════════════════════");
        System.out.println("📦 TOKEN LOCK DETECTED");
        System.out.println("═══════════════════════════════════════════");
        System.out.println("Token Address:    " + event.token);
        System.out.println("Sender:           " + event.sender);
        System.out.println("Amount:           " + event.amount);
        System.out.println("Block Timestamp:  " + event.blockTimestamp);
        System.out.println("Mirror Timestamp: " + event.timestamp);
        System.out.println("═══════════════════════════════════════════");
    }

    static class TokenLockEvent {
        String token = "";
        String sender = "";
        long amount = 0;
        long blockTimestamp = 0;
        String timestamp = "";
    }
}
