export default class ExchangeRates {
    /**
     * @internal
     * @param {proto.IExchangeRateSet} rateSet
     * @returns {ExchangeRates}
     */
    static _fromProtobuf(rateSet: proto.IExchangeRateSet): ExchangeRates;
    /**
     * @param {Uint8Array} bytes
     * @returns {ExchangeRates}
     */
    static fromBytes(bytes: Uint8Array): ExchangeRates;
    /**
     * @private
     * @param {object} props
     * @param {ExchangeRate} props.currentRate
     * @param {ExchangeRate} props.nextRate
     */
    private constructor();
    /**
     * @readonly
     */
    readonly currentRate: ExchangeRate;
    /**
     * @readonly
     */
    readonly nextRate: ExchangeRate;
    /**
     * @internal
     * @returns {proto.IExchangeRateSet}
     */
    _toProtobuf(): proto.IExchangeRateSet;
}
import ExchangeRate from "./ExchangeRate.js";
import * as proto from "@hashgraph/proto";
