"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _Signer = _interopRequireDefault(require("./Signer.cjs"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

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
class Wallet extends _Signer.default {
  /**
   * @protected
   */
  constructor() {
    super();
  }
  /**
   * @abstract
   * @returns {Provider}
   */


  getProvider() {
    throw new Error("not implemented");
  }
  /**
   * @abstract
   * @returns {Key}
   */


  getAccountKey() {
    throw new Error("not implemented");
  }

}

exports.default = Wallet;