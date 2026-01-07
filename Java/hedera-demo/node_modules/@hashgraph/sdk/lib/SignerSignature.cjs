"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

/**
 * @typedef {import("./PublicKey.js").default} PublicKey
 * @typedef {import("./account/AccountId.js").default} AccountId
 */
class SignerSignature {
  /**
   * @param {object} props
   * @param {PublicKey} props.publicKey
   * @param {Uint8Array} props.signature
   * @param {AccountId} props.accountId
   */
  constructor(props) {
    this.publicKey = props.publicKey;
    this.signature = props.signature;
    this.accountId = props.accountId;
  }

}

exports.default = SignerSignature;