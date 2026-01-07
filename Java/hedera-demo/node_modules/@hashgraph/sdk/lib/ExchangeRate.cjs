"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _long = _interopRequireDefault(require("long"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

class ExchangeRate {
  /**
   * @private
   * @param {object} props
   * @param {number} props.hbars
   * @param {number} props.cents
   * @param {Date} props.expirationTime
   */
  constructor(props) {
    /**
     * Denotes Hbar equivalent to cents (USD)
     *
     * @readonly
     * @type {number}
     */
    this.hbars = props.hbars;
    /**
     * Denotes cents (USD) equivalent to Hbar
     *
     * @readonly
     * @type {number}
     */

    this.cents = props.cents;
    /**
     * Expiration time of this exchange rate
     *
     * @readonly
     * @type {Date}
     */

    this.expirationTime = props.expirationTime;
    /**
     * Calculated exchange rate
     *
     * @readonly
     * @type {number}
     */

    this.exchangeRateInCents = props.cents / props.hbars;
    Object.freeze(this);
  }
  /**
   * @internal
   * @param {import("@hashgraph/proto").IExchangeRate} rate
   * @returns {ExchangeRate}
   */


  static _fromProtobuf(rate) {
    return new ExchangeRate({
      hbars:
      /** @type {number} */
      rate.hbarEquiv,
      cents:
      /** @type {number} */
      rate.centEquiv,
      expirationTime: new Date(rate.expirationTime != null ? rate.expirationTime.seconds != null ? _long.default.isLong(rate.expirationTime.seconds) ? rate.expirationTime.seconds.toInt() : rate.expirationTime.seconds : 0 * 1000 : 0 * 1000)
    });
  }
  /**
   * @internal
   * @returns {import("@hashgraph/proto").IExchangeRate}
   */


  _toProtobuf() {
    return {
      hbarEquiv: this.hbars,
      centEquiv: this.cents,
      expirationTime: {
        seconds: _long.default.fromNumber(this.expirationTime.getSeconds())
      }
    };
  }

}

exports.default = ExchangeRate;