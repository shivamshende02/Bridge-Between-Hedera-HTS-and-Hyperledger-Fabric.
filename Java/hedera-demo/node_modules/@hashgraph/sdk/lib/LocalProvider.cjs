"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _NodeClient = _interopRequireDefault(require("./client/NodeClient.cjs"));

var _Provider = _interopRequireDefault(require("./Provider.cjs"));

var _AccountBalanceQuery = _interopRequireDefault(require("./account/AccountBalanceQuery.cjs"));

var _AccountInfoQuery = _interopRequireDefault(require("./account/AccountInfoQuery.cjs"));

var _AccountRecordsQuery = _interopRequireDefault(require("./account/AccountRecordsQuery.cjs"));

var _TransactionReceiptQuery = _interopRequireDefault(require("./transaction/TransactionReceiptQuery.cjs"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

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
class LocalProvider extends _Provider.default {
  constructor() {
    super();

    if (process.env.HEDERA_NETWORK == null) {
      throw new Error("LocalProvider requires the `HEDERA_NETWORK` environment variable to be set");
    }

    this._client = _NodeClient.default.forName(process.env.HEDERA_NETWORK);
  }
  /**
   * @returns {LedgerId?}
   */


  getLedgerId() {
    return this._client.ledgerId;
  }
  /**
   * @returns {{[key: string]: (string | AccountId)}}
   */


  getNetwork() {
    return this._client.network;
  }
  /**
   * @returns {string[]}
   */


  getMirrorNetwork() {
    return this._client.mirrorNetwork;
  }
  /**
   * @param {AccountId | string} accountId
   * @returns {Promise<AccountBalance>}
   */


  getAccountBalance(accountId) {
    return new _AccountBalanceQuery.default().setAccountId(accountId).execute(this._client);
  }
  /**
   * @param {AccountId | string} accountId
   * @returns {Promise<AccountInfo>}
   */


  getAccountInfo(accountId) {
    return new _AccountInfoQuery.default().setAccountId(accountId).execute(this._client);
  }
  /**
   * @param {AccountId | string} accountId
   * @returns {Promise<TransactionRecord[]>}
   */


  getAccountRecords(accountId) {
    return new _AccountRecordsQuery.default().setAccountId(accountId).execute(this._client);
  }
  /**
   * @param {TransactionId | string} transactionId
   * @returns {Promise<TransactionReceipt>}
   */


  getTransactionReceipt(transactionId) {
    return new _TransactionReceiptQuery.default().setTransactionId(transactionId).execute(this._client);
  }
  /**
   * @param {TransactionResponse} response
   * @returns {Promise<TransactionReceipt>}
   */


  waitForReceipt(response) {
    return new _TransactionReceiptQuery.default().setNodeAccountIds([response.nodeId]).setTransactionId(response.transactionId).execute(this._client);
  }
  /**
   * @template RequestT
   * @template ResponseT
   * @template OutputT
   * @param {Executable<RequestT, ResponseT, OutputT>} request
   * @returns {Promise<OutputT>}
   */


  sendRequest(request) {
    return request.execute(this._client);
  }

}

exports.default = LocalProvider;