// SPDX-License-Identifier: Apache-2.0
pragma solidity ^0.8.20;

import "./IHederaTokenService.sol";

/// @title Hedera Escrow Contract for Cross-Chain Interoperability with Token Creation
/// @notice Supports token creation, minting, association, locking, and release with event triggers
/// @dev Compatible with IHederaTokenService precompile (0x167)
contract HederaEscrow {
    address public owner;        // Contract deployer (optional control)
    address public beneficiary;  // Default beneficiary (optional)
    address constant HTS = address(0x167); // Hedera Token Service precompile address

    // Mapping to track tokens created by this contract
    mapping(address => bool) public createdTokens;

    // --------------------------- Events ---------------------------

    event TokenCreated(address indexed tokenAddress, string name, string symbol);
    event TokenMinted(address indexed tokenAddress, uint64 newTotalSupply);
    event TokenAssociated(address indexed token);
    event TokensLocked(
        address indexed token,
        address indexed sender,
        int64 amount,
        uint256 timestamp
    );
    event TokensReleased(address indexed token, address indexed recipient, int64 amount);
    event TokenTransferFailed(address indexed token, address indexed recipient, int64 code);
    event DebugResponse(int64 code);

    // --------------------------- Constructor ---------------------------

    constructor(address _beneficiary) {
        owner = msg.sender;
        beneficiary = _beneficiary;
    }

    // --------------------------- Token Creation via SDK ---------------------------
    
    /**
     * @notice Records a token created externally (via Hedera SDK)
     * @dev Use this after creating a token with the SDK, making this contract the treasury
     * @param tokenAddress The address of the token created via SDK
     */
    function registerCreatedToken(address tokenAddress) external {
        require(msg.sender == owner, "Only owner can register tokens");
        createdTokens[tokenAddress] = true;
        emit TokenCreated(tokenAddress, "External", "EXT");
    }

    // --------------------------- Low-Level Token Creation ---------------------------

    /**
     * @notice Creates a new fungible token using low-level call
     * @dev This bypasses struct issues by using direct encoding
     * @param name Token name
     * @param symbol Token symbol
     * @param initialSupply Initial supply
     * @param decimals Token decimals
     */
    function createFungibleTokenLowLevel(
        string memory name,
        string memory symbol,
        uint64 initialSupply,
        uint32 decimals
    ) external payable returns (address tokenAddress) {
        
        // Function selector for createFungibleToken
        bytes4 selector = bytes4(keccak256("createFungibleToken((string,string,address,string,bool,int64,bool,(uint256,(bool,address,bytes,bytes,address))[],(int64,address,int64)),uint64,uint32)"));
        
        // This is a simplified approach - encode minimal token data
        bytes memory data = abi.encodeWithSelector(
            selector,
            name,
            symbol,
            address(this), // treasury
            "",            // memo
            false,         // tokenSupplyType (infinite)
            0,             // maxSupply
            false,         // freezeDefault
            initialSupply,
            decimals
        );

        (bool success, bytes memory result) = HTS.call{value: msg.value}(data);
        
        if (success && result.length > 0) {
            (int64 responseCode, address newToken) = abi.decode(result, (int64, address));
            emit DebugResponse(responseCode);
            
            if (responseCode == 22) { // SUCCESS
                createdTokens[newToken] = true;
                emit TokenCreated(newToken, name, symbol);
                return newToken;
            }
        }
        
        revert("Token creation failed");
    }

    // --------------------------- Token Minting ---------------------------

    /**
     * @notice Mints additional tokens using low-level call
     * @param token Address of the token to mint
     * @param amount Amount to mint
     */
    function mintTokens(address token, uint64 amount) external returns (uint64) {
        require(amount > 0, "Mint amount must be greater than zero");
        require(createdTokens[token], "Token not created by this contract");

        // mintToken function selector
        bytes4 selector = bytes4(keccak256("mintToken(address,uint64,bytes[])"));
        bytes memory data = abi.encodeWithSelector(selector, token, amount, new bytes[](0));

        (bool success, bytes memory result) = HTS.call(data);
        
        if (success && result.length > 0) {
            (int64 responseCode, uint64 newTotalSupply, ) = abi.decode(result, (int64, uint64, int64[]));
            emit DebugResponse(responseCode);
            
            if (responseCode == 22) { // SUCCESS
                emit TokenMinted(token, newTotalSupply);
                return newTotalSupply;
            }
        }
        
        revert("Minting failed");
    }

    // --------------------------- Association ---------------------------

    /// Anyone can associate this contract with a token
    function associateNewToken(address token) external {
        int64 rc = IHederaTokenService(HTS).associateToken(address(this), token);
        emit DebugResponse(rc);
        require(rc == HederaResponseCodes.SUCCESS, "Token association failed");
        emit TokenAssociated(token);
    }

    // --------------------------- Lock Tokens ---------------------------

    /**
     * @notice Logs a token lock event for cross-chain bridge verification.
     * @dev The user must transfer tokens to the contract externally (via SDK or backend).
     * @param token The address of the HTS token.
     * @param amount The amount of tokens locked.
     */
    function lockTokens(address token, int64 amount) external {
        require(amount > 0, "Lock amount must be greater than zero");

        // Emit detailed lock event (used by Mirror Node listener)
        emit TokensLocked(token, msg.sender, amount, block.timestamp);
    }

    // --------------------------- Release Tokens ---------------------------

    /// Core release: transfers from escrow contract to a recipient (mirror or alias address)
    function releaseToAddress(address token, address recipient, int64 amount) public {
        require(amount > 0, "Release amount must be greater than zero");

        int64 rc = IHederaTokenService(HTS).transferToken(
            token,
            address(this),
            recipient,
            amount
        );

        emit DebugResponse(rc); // Emits the raw HTS response code for diagnostics

        if (rc != HederaResponseCodes.SUCCESS) {
            emit TokenTransferFailed(token, recipient, rc);
        }

        require(rc == HederaResponseCodes.SUCCESS, "Token transfer failed");

        emit TokensReleased(token, recipient, amount);
    }

    /// Backward-compatible wrapper (preserves old `releaseTokens` call signature)
    function releaseTokens(address token, address recipient, int64 amount) external {
        releaseToAddress(token, recipient, amount);
    }

    /// Safer path: release by Hedera AccountId (shard, realm, num)
    /// Avoids INVALID_ALIAS_KEY for ED25519 accounts.
    function releaseByAccountId(
        address token,
        uint32 shard,
        uint64 realm,
        uint64 num,
        int64 amount
    ) external {
        address recipient = _accountIdToMirrorAddress(shard, realm, num);
        releaseToAddress(token, recipient, amount);
    }

    // --------------------------- Internal Utilities ---------------------------

    /// Convert Hedera AccountId → mirror Solidity address (20 bytes)
    function _accountIdToMirrorAddress(
        uint32 shard,
        uint64 realm,
        uint64 num
    ) internal pure returns (address addr) {
        bytes memory packed = abi.encodePacked(shard, realm, num); // 4 + 8 + 8 = 20 bytes
        assembly {
            addr := mload(add(packed, 20)) // read last 20 bytes as address
        }
    }

    // Allow contract to receive HBAR for token creation fees
    receive() external payable {}
}
