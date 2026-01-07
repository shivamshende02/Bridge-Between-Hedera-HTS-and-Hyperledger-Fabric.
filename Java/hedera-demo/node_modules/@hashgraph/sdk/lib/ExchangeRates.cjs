"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _ExchangeRate = _interopRequireDefault(require("./ExchangeRate.cjs"));

var proto = _interopRequireWildcard(require("@hashgraph/proto"));

function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }

function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

class ExchangeRates {
  /**
   * @private
   * @param {object} props
   * @param {ExchangeRate} props.currentRate
   * @param {ExchangeRate} props.nextRate
   */
  constructor(props) {
    /**
     * @readonly
     */
    this.currentRate = props.currentRate;
    /**
     * @readonly
     */

    this.nextRate = props.nextRate;
    Object.freeze(this);
  }
  /**
   * @internal
   * @param {proto.IExchangeRateSet} rateSet
   * @returns {ExchangeRates}
   */


  static _fromProtobuf(rateSet) {
    return new ExchangeRates({
      currentRate: _ExchangeRate.default._fromProtobuf(
      /** @type {proto.IExchangeRate} */
      rateSet.currentRate),
      nextRate: _ExchangeRate.default._fromProtobuf(
      /** @type {proto.IExchangeRate} */
      rateSet.nextRate)
    });
  }
  /**
   * @internal
   * @returns {proto.IExchangeRateSet}
   */


  _toProtobuf() {
    return {
      currentRate: this.currentRate._toProtobuf(),
      nextRate: this.nextRate._toProtobuf()
    };
  }
  /**
   * @param {Uint8Array} bytes
   * @returns {ExchangeRates}
   */


  static fromBytes(bytes) {
    return ExchangeRates._fromProtobuf(proto.ExchangeRateSet.decode(bytes));
  }

}

exports.default = ExchangeRates;