/**
 * @typedef {import("./LedgerId.js").default} LedgerId
 * @typedef {import("./transaction/TransactionId.js").default} TransactionId
 * @typedef {import("./transaction/Transaction.js").default} Transaction
 * @typedef {import("./transaction/TransactionResponse.js").default} TransactionResponse
 * @typedef {import("./transaction/TransactionReceipt.js").default} TransactionReceipt
 * @typedef {import("./transaction/TransactionRecord.js").default} TransactionRecord
 * @typedef {import("./account/AccountId.js").default} AccountId
 * @typedef {import("./account/AccountBalance.js").default} AccountBalance
 * @typedef {import("./account/AccountInfo.js").default} AccountInfo
 */
/**
 * @template O
 * @typedef {import("./query/Query.js").default<O>} Query<O>
 */
/**
 * @template RequestT
 * @template ResponseT
 * @template OutputT
 * @typedef {import("./Executable.js").default<RequestT, ResponseT, OutputT>} Executable<RequestT, ResponseT, OutputT>
 */
/**
 * @abstract
 */
export default class Provider {
    /**
     * @abstract
     * @returns {LedgerId?}
     */
    getLedgerId(): LedgerId | null;
    /**
     * @abstract
     * @returns {{[key: string]: (string | AccountId)}}
     */
    getNetwork(): {
        [key: string]: string | import("./account/AccountId.js").default;
    };
    /**
     * @abstract
     * @returns {string[]}
     */
    getMirrorNetwork(): string[];
    /**
     * @abstract
     * @param {AccountId | string} accountId
     * @returns {Promise<AccountBalance>}
     */
    getAccountBalance(accountId: AccountId | string): Promise<AccountBalance>;
    /**
     * @abstract
     * @param {AccountId | string} accountId
     * @returns {Promise<AccountInfo>}
     */
    getAccountInfo(accountId: AccountId | string): Promise<AccountInfo>;
    /**
     * @abstract
     * @param {AccountId | string} accountId
     * @returns {Promise<TransactionRecord[]>}
     */
    getAccountRecords(accountId: AccountId | string): Promise<TransactionRecord[]>;
    /**
     * @abstract
     * @param {TransactionId | string} transactionId
     * @returns {Promise<TransactionReceipt>}
     */
    getTransactionReceipt(transactionId: TransactionId | string): Promise<TransactionReceipt>;
    /**
     * @abstract
     * @param {TransactionResponse} response
     * @returns {Promise<TransactionReceipt>}
     */
    waitForReceipt(response: TransactionResponse): Promise<TransactionReceipt>;
    /**
     * @abstract
     * @template RequestT
     * @template ResponseT
     * @template OutputT
     * @param {Executable<RequestT, ResponseT, OutputT>} request
     * @returns {Promise<OutputT>}
     */
    sendRequest<RequestT, ResponseT, OutputT>(request: import("./Executable.js").default<RequestT, ResponseT, OutputT>): Promise<OutputT>;
}
export type LedgerId = import("./LedgerId.js").default;
export type TransactionId = import("./transaction/TransactionId.js").default;
export type Transaction = import("./transaction/Transaction.js").default;
export type TransactionResponse = import("./transaction/TransactionResponse.js").default;
export type TransactionReceipt = import("./transaction/TransactionReceipt.js").default;
export type TransactionRecord = import("./transaction/TransactionRecord.js").default;
export type AccountId = import("./account/AccountId.js").default;
export type AccountBalance = import("./account/AccountBalance.js").default;
export type AccountInfo = import("./account/AccountInfo.js").default;
/**
 * <O>
 */
export type Query<O> = import("./query/Query.js").default<O>;
/**
 * <RequestT, ResponseT, OutputT>
 */
export type Executable<RequestT, ResponseT, OutputT> = import("./Executable.js").default<RequestT, ResponseT, OutputT>;
