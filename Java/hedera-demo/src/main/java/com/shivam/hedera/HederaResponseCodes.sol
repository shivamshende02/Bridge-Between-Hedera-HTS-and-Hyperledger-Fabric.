// SPDX-License-Identifier: Apache-2.0
pragma solidity ^0.8.20;

contract HederaResponseCodes {
    int64 constant SUCCESS = 22;
    int64 constant INVALID_ACCOUNT_ID = 1;
    int64 constant INVALID_TOKEN_ID = 2;
    int64 constant INVALID_TRANSACTION = 3;
    int64 constant TOKEN_NOT_ASSOCIATED_TO_ACCOUNT = 23;
    int64 constant INSUFFICIENT_TOKEN_BALANCE = 24;
    int64 constant TOKEN_WAS_DELETED = 25;
    int64 constant ACCOUNT_FROZEN_FOR_TOKEN = 26;
    int64 constant TOKEN_HAS_NO_SUPPLY_KEY = 27;
    int64 constant TOKEN_HAS_NO_WIPE_KEY = 28;
    int64 constant INVALID_SIGNATURE = 29;
    int64 constant INVALID_FREEZE_TRANSACTION_BODY = 30;
    int64 constant TRANSFER_LIST_SIZE_LIMIT_EXCEEDED = 31;
    int64 constant INVALID_ACCOUNT_AMOUNTS = 32;
    int64 constant NEGATIVE_ACCOUNT_BALANCES = 33;
    int64 constant TOKEN_ALREADY_ASSOCIATED_TO_ACCOUNT = 34;
}
