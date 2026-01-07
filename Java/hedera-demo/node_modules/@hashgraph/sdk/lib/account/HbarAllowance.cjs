"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _AccountId = _interopRequireDefault(require("./AccountId.cjs"));

var _Hbar = _interopRequireDefault(require("../Hbar.cjs"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * @namespace proto
 * @typedef {import("@hashgraph/proto").ICryptoAllowance} proto.ICryptoAllowance
 * @typedef {import("@hashgraph/proto").IGrantedCryptoAllowance} proto.IGrantedCryptoAllowance
 * @typedef {import("@hashgraph/proto").IAccountID} proto.IAccountID
 */

/**
 * @typedef {import("long")} Long
 */
class HbarAllowance {
  /**
   * @internal
   * @param {object} props
   * @param {AccountId} props.spenderAccountId
   * @param {AccountId | null} props.ownerAccountId
   * @param {Hbar} props.amount
   */
  constructor(props) {
    /**
     * The account ID of the hbar allowance spender.
     *
     * @readonly
     */
    this.spenderAccountId = props.spenderAccountId;
    /**
     * The account ID of the hbar allowance owner.
     *
     * @readonly
     */

    this.ownerAccountId = props.ownerAccountId;
    /**
     * The current balance of the spender's allowance in tinybars.
     *
     * @readonly
     */

    this.amount = props.amount;
    Object.freeze(this);
  }
  /**
   * @internal
   * @param {proto.ICryptoAllowance} approval
   * @returns {HbarAllowance}
   */


  static _fromProtobuf(approval) {
    return new HbarAllowance({
      spenderAccountId: _AccountId.default._fromProtobuf(
      /** @type {proto.IAccountID} */
      approval.spender),
      ownerAccountId: approval.owner != null ? _AccountId.default._fromProtobuf(
      /**@type {proto.IAccountID}*/
      approval.owner) : null,
      amount: _Hbar.default.fromTinybars(approval.amount != null ? approval.amount : 0)
    });
  }
  /**
   * @internal
   * @param {proto.IGrantedCryptoAllowance} approval
   * @returns {HbarAllowance}
   */


  static _fromGrantedProtobuf(approval) {
    return new HbarAllowance({
      spenderAccountId: _AccountId.default._fromProtobuf(
      /** @type {proto.IAccountID} */
      approval.spender),
      ownerAccountId: null,
      amount: _Hbar.default.fromTinybars(approval.amount != null ? approval.amount : 0)
    });
  }
  /**
   * @internal
   * @returns {proto.ICryptoAllowance}
   */


  _toProtobuf() {
    return {
      spender: this.spenderAccountId._toProtobuf(),
      owner: this.ownerAccountId != null ? this.ownerAccountId._toProtobuf() : null,
      amount: this.amount.toTinybars()
    };
  }

}

exports.default = HbarAllowance;