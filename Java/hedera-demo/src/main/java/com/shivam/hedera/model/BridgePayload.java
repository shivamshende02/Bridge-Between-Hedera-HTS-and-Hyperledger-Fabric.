package com.shivam.hedera.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BridgePayload {
    
    private String network = "hedera-testnet";
    private String contractId;
    private String contractEvmAddress;
    private String tokenId;
    private String tokenEvmAddress;
    private String senderAccountId;
    private String senderEvmAddress;
    private long amount;
    private String consensusTimestamp;
    private String blockTimestamp;
    private String eventSignature;
    private String mirrorNodeEventUrl;
    private String bridgeRequestId;
    private long createdAt;
    
    public BridgePayload() {
        this.createdAt = System.currentTimeMillis();
        this.bridgeRequestId = "HEDERA_TO_HLF_" + System.currentTimeMillis() + "_" + 
                               Math.abs(new java.util.Random().nextInt(10000));
    }
    
    public String getNetwork() { return network; }
    public void setNetwork(String network) { this.network = network; }
    
    public String getContractId() { return contractId; }
    public void setContractId(String contractId) { this.contractId = contractId; }
    
    public String getContractEvmAddress() { return contractEvmAddress; }
    public void setContractEvmAddress(String contractEvmAddress) { this.contractEvmAddress = contractEvmAddress; }
    
    public String getTokenId() { return tokenId; }
    public void setTokenId(String tokenId) { this.tokenId = tokenId; }
    
    public String getTokenEvmAddress() { return tokenEvmAddress; }
    public void setTokenEvmAddress(String tokenEvmAddress) { this.tokenEvmAddress = tokenEvmAddress; }
    
    public String getSenderAccountId() { return senderAccountId; }
    public void setSenderAccountId(String senderAccountId) { this.senderAccountId = senderAccountId; }
    
    public String getSenderEvmAddress() { return senderEvmAddress; }
    public void setSenderEvmAddress(String senderEvmAddress) { this.senderEvmAddress = senderEvmAddress; }
    
    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }
    
    public String getConsensusTimestamp() { return consensusTimestamp; }
    public void setConsensusTimestamp(String consensusTimestamp) { this.consensusTimestamp = consensusTimestamp; }
    
    public String getBlockTimestamp() { return blockTimestamp; }
    public void setBlockTimestamp(String blockTimestamp) { this.blockTimestamp = blockTimestamp; }
    
    public String getEventSignature() { return eventSignature; }
    public void setEventSignature(String eventSignature) { this.eventSignature = eventSignature; }
    
    public String getMirrorNodeEventUrl() { return mirrorNodeEventUrl; }
    public void setMirrorNodeEventUrl(String mirrorNodeEventUrl) { this.mirrorNodeEventUrl = mirrorNodeEventUrl; }
    
    public String getBridgeRequestId() { return bridgeRequestId; }
    public void setBridgeRequestId(String bridgeRequestId) { this.bridgeRequestId = bridgeRequestId; }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
