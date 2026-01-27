package com.shivam.hedera.bridge;

import com.shivam.hedera.model.BridgePayload;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HLFBridgeClient {
    
    private final String hlfApiEndpoint;
    private final HttpClient httpClient;
    
    public HLFBridgeClient(String hlfApiEndpoint) {
        this.hlfApiEndpoint = hlfApiEndpoint;
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    }
    
    public boolean sendLockEvent(BridgePayload payload) {
        try {
            System.out.println("\n🌉 Sending to HLF Bridge...");
            System.out.println("Endpoint: " + hlfApiEndpoint);
            System.out.println("Request ID: " + payload.getBridgeRequestId());
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(hlfApiEndpoint + "/api/bridge/mint"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toJson()))
                .timeout(Duration.ofSeconds(30))
                .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("✅ HLF Response: " + response.statusCode());
                System.out.println("Response Body: " + response.body());
                return true;
            } else {
                System.err.println("❌ HLF Error: " + response.statusCode());
                System.err.println("Response: " + response.body());
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("❌ Failed to send to HLF: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
