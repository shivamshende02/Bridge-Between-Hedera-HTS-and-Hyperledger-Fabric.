"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _Cache = _interopRequireDefault(require("../Cache.cjs"));

var _ContractId = _interopRequireDefault(require("./ContractId.cjs"));

var hex = _interopRequireWildcard(require("../encoding/hex.cjs"));

function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }

function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * @namespace {proto}
 * @typedef {import("@hashgraph/proto").IContractID} proto.IContractID
 * @typedef {import("@hashgraph/proto").IKey} proto.IKey
 */

/**
 * @typedef {import("long").Long} Long
 * @typedef {import("../client/Client.js").default<*, *>} Client
 */
class DelegateContractId extends _ContractId.default {
  /**
   * @param {number | Long | import("../EntityIdHelper").IEntityId} props
   * @param {(number | Long)=} realm
   * @param {(number | Long)=} num
   * @param {Uint8Array=} evmAddress
   */
  constructor(props, realm, num, evmAddress) {
    super(props, realm, num, evmAddress);
  }
  /**
   * @param {Long | number} shard
   * @param {Long | number} realm
   * @param {string} evmAddress
   * @returns {ContractId}
   */


  static fromEvmAddress(shard, realm, evmAddress) {
    return new DelegateContractId(shard, realm, 0, hex.decode(evmAddress));
  }
  /**
   * @param {string} text
   * @returns {DelegateContractId}
   */


  static fromString(text) {
    return new DelegateContractId(_ContractId.default.fromString(text));
  }
  /**
   * @internal
   * @param {proto.IContractID} id
   * @returns {DelegateContractId}
   */


  static _fromProtobuf(id) {
    return new DelegateContractId(_ContractId.default._fromProtobuf(id));
  }
  /**
   * @param {Uint8Array} bytes
   * @returns {DelegateContractId}
   */


  static fromBytes(bytes) {
    return new DelegateContractId(_ContractId.default.fromBytes(bytes));
  }
  /**
   * @deprecated - Use `DelegateContractId.fromEvmAddress()` instead
   * @param {string} address
   * @returns {DelegateContractId}
   */


  static fromSolidityAddress(address) {
    // eslint-disable-next-line deprecation/deprecation
    return new DelegateContractId(_ContractId.default.fromSolidityAddress(address));
  }
  /**
   * @returns {DelegateContractId}
   */


  clone() {
    const id = new DelegateContractId(this);
    id._checksum = this._checksum;
    return id;
  }
  /**
   * @returns {proto.IKey}
   */


  _toProtobufKey() {
    return {
      delegatableContractId: this._toProtobuf()
    };
  }
  /**
   * @param {proto.IContractID} key
   * @returns {DelegateContractId}
   */


  static __fromProtobufKey(key) {
    return DelegateContractId._fromProtobuf(key);
  }

}

exports.default = DelegateContractId;

_Cache.default.delegateContractId = key => DelegateContractId.__fromProtobufKey(key);