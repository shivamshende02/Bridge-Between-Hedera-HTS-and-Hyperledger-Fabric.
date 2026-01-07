/**
 * @typedef {import("./LedgerId.js").default} LedgerId
 * @typedef {import("./Key.js").default} Key
 * @typedef {import("./transaction/Transaction.js").default} Transaction
 * @typedef {import("./transaction/TransactionId.js").default} TransactionId
 * @typedef {import("./transaction/TransactionResponse.js").default} TransactionResponse
 * @typedef {import("./transaction/TransactionReceipt.js").default} TransactionReceipt
 * @typedef {import("./transaction/TransactionRecord.js").default} TransactionRecord
 * @typedef {import("./account/AccountId.js").default} AccountId
 * @typedef {import("./account/AccountBalance.js").default} AccountBalance
 * @typedef {import("./account/AccountInfo.js").default} AccountInfo
 */
/**
 * @template {any} O
 * @typedef {import("./query/Query.js").default<O>} Query<O>
 */
/**
 * @template RequestT
 * @template ResponseT
 * @template OutputT
 * @typedef {import("./Executable.js").default<RequestT, ResponseT, OutputT>} Executable<RequestT, ResponseT, OutputT>
 */
export default class LocalProvider extends Provider {
    _client: Client;
}
export type LedgerId = import("./LedgerId.js").default;
export type Key = import("./Key.js").default;
export type Transaction = import("./transaction/Transaction.js").default;
export type TransactionId = import("./transaction/TransactionId.js").default;
export type TransactionResponse = import("./transaction/TransactionResponse.js").default;
export type TransactionReceipt = import("./transaction/TransactionReceipt.js").default;
export type TransactionRecord = import("./transaction/TransactionRecord.js").default;
export type AccountId = import("./account/AccountId.js").default;
export type AccountBalance = import("./account/AccountBalance.js").default;
export type AccountInfo = import("./account/AccountInfo.js").default;
/**
 * <O>
 */
export type Query<O extends unknown> = import("./query/Query.js").default<O>;
/**
 * <RequestT, ResponseT, OutputT>
 */
export type Executable<RequestT, ResponseT, OutputT> = import("./Executable.js").default<RequestT, ResponseT, OutputT>;
import Provider from "./Provider.js";
import Client from "./client/NodeClient.js";
