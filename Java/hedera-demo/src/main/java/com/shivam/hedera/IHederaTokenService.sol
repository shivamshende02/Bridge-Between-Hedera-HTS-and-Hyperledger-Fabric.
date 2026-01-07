// SPDX-License-Identifier: Apache-2.0
pragma solidity ^0.8.20;

interface IHederaTokenService {
    function associateToken(address account, address token) external returns (int64 responseCode);
    function transferToken(address token, address sender, address recipient, int64 amount) external returns (int64 responseCode);
}

library HederaResponseCodes {
    int constant SUCCESS = 22;
}
