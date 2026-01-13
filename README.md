Hedera–Hyperledger Fabric Token Bridge
A high-integrity asset bridge between Hedera Token Service (HTS) and Hyperledger Fabric, enabling secure lock–mint and burn–unlock flows with deterministic attestations and sub‑2s event ingestion. HTS tokens on Hedera are mirrored as Fabric assets, allowing enterprise networks to interoperate with a fast, public DLT.
​

Features
HTS ↔ Fabric token bridge using a Solidity HederaEscrow contract (HTS precompile) and Fabric chaincode for asset mint/burn.
​

Event‑driven architecture with a Mirror Node listener achieving near‑real‑time ingestion and 100% log delivery in tests.
​

Deterministic orchestration API (Node.js/Java) coordinating lock/mint and burn/unlock with strong payload validation.

Configurable trust model for permissioned Fabric networks while leveraging Hedera’s public trust and finality.
​

Extensible design for supporting multiple token classes and networks in future.

Architecture
The bridge follows a classic lock‑and‑mint / burn‑and‑unlock pattern adapted to Hedera and Fabric.
​

Hedera side

HederaEscrow Solidity contract interacts with HTS via the precompiled contract (HIP‑206) to lock and release tokens.
​

Emits structured events for each lock/unlock operation, consumed by the Mirror Node listener.
​

Fabric side

Fabric chaincode defines a fungible token/asset model mirroring the locked HTS token supply.
​

Bridge client uses Fabric SDK to submit deterministic mint/burn transactions into the permissioned network.
​

Bridge services

<img width="975" height="650" alt="image" src="https://github.com/user-attachments/assets/2a0b25af-ce67-4304-bf15-96c024ba80b8" />


Mirror Node Listener: Polls Hedera Mirror Node, decodes ABI logs, and forwards validated events to the Fabric bridge API.
​

Bridge API: Stateless REST (Node.js/Java) that validates proofs, enforces replay protection, and orchestrates Fabric transactions.

Token Flow
1. Hedera → Fabric (Lock & Mint)
User calls lockTokens on HederaEscrow with amount and destination Fabric identity.

Contract locks HTS tokens and emits a TokensLocked event (including tx hash, token ID, amount, receiver).

Mirror Node Listener detects and decodes the event, then calls the Bridge API.

Bridge API submits a mint transaction to Fabric chaincode, crediting the mapped asset to the Fabric user.

Client/UI updates balances on both sides accordingly.

2. Fabric → Hedera (Burn & Unlock)
User requests withdrawal from the Fabric network via the bridge client.

Fabric chaincode burns the bridged asset and emits a Fabric event or writes a withdrawal record.

Bridge API verifies the state/event, then submits an unlock transaction to HederaEscrow.

Contract releases HTS tokens back to the user’s Hedera account and emits TokensUnlocked.

Mirror Node finalization confirms the operation for off‑chain services.

1. Prerequisites
Node.js (LTS) and npm/yarn.

Java 11+ (if Bridge API or Fabric client is in Java).

Docker and Docker Compose for local Fabric network.
​

Access to a Hedera testnet account and operator keys.
​

hedera-sdk and required Fabric SDK dependencies.
Create a .env file for each component (examples in config/.env.example):

Hedera / Mirror Listener

HEDERA_NETWORK=testnet

HEDERA_OPERATOR_ID=...

HEDERA_OPERATOR_KEY=...

MIRROR_NODE_URL=https://testnet.mirrornode.hedera.com/api/v1


   
