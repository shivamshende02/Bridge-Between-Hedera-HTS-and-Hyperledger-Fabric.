/**
 * @typedef {import("./LedgerId.js").default} LedgerId
 * @typedef {import("./SignerSignature.js").default} SignerSignature
 * @typedef {import("./Provider.js").default} Provider
 * @typedef {import("./Key.js").default} Key
 * @typedef {import("./transaction/TransactionId.js").default} TransactionId
 * @typedef {import("./transaction/Transaction.js").default} Transaction
 * @typedef {import("./transaction/TransactionResponse.js").default} TransactionResponse
 * @typedef {import("./transaction/TransactionReceipt.js").default} TransactionReceipt
 * @typedef {import("./account/AccountId.js").default} AccountId
 * @typedef {import("./account/AccountBalance.js").default} AccountBalance
 * @typedef {import("./account/AccountInfo.js").default} AccountInfo
 */
/**
 * @template {any} O
 * @typedef {import("./query/Query.js").default<O>} Query<O>
 */
/**
 * @abstract
 */
export default class Wallet extends Signer {
    /**
     * @abstract
     * @returns {Provider}
     */
    getProvider(): Provider;
    /**
     * @abstract
     * @returns {Key}
     */
    getAccountKey(): Key;
}
export type LedgerId = import("./LedgerId.js").default;
export type SignerSignature = import("./SignerSignature.js").default;
export type Provider = import("./Provider.js").default;
export type Key = import("./Key.js").default;
export type TransactionId = import("./transaction/TransactionId.js").default;
export type Transaction = import("./transaction/Transaction.js").default;
export type TransactionResponse = import("./transaction/TransactionResponse.js").default;
export type TransactionReceipt = import("./transaction/TransactionReceipt.js").default;
export type AccountId = import("./account/AccountId.js").default;
export type AccountBalance = import("./account/AccountBalance.js").default;
export type AccountInfo = import("./account/AccountInfo.js").default;
/**
 * <O>
 */
export type Query<O extends unknown> = import("./query/Query.js").default<O>;
import Signer from "./Signer.js";
