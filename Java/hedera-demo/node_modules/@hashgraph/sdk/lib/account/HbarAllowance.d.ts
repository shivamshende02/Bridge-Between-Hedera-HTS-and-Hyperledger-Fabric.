/**
 * @namespace proto
 * @typedef {import("@hashgraph/proto").ICryptoAllowance} proto.ICryptoAllowance
 * @typedef {import("@hashgraph/proto").IGrantedCryptoAllowance} proto.IGrantedCryptoAllowance
 * @typedef {import("@hashgraph/proto").IAccountID} proto.IAccountID
 */
/**
 * @typedef {import("long")} Long
 */
export default class HbarAllowance {
    /**
     * @internal
     * @param {proto.ICryptoAllowance} approval
     * @returns {HbarAllowance}
     */
    static _fromProtobuf(approval: proto.ICryptoAllowance): HbarAllowance;
    /**
     * @internal
     * @param {proto.IGrantedCryptoAllowance} approval
     * @returns {HbarAllowance}
     */
    static _fromGrantedProtobuf(approval: proto.IGrantedCryptoAllowance): HbarAllowance;
    /**
     * @internal
     * @param {object} props
     * @param {AccountId} props.spenderAccountId
     * @param {AccountId | null} props.ownerAccountId
     * @param {Hbar} props.amount
     */
    constructor(props: {
        spenderAccountId: AccountId;
        ownerAccountId: AccountId | null;
        amount: Hbar;
    });
    /**
     * The account ID of the hbar allowance spender.
     *
     * @readonly
     */
    readonly spenderAccountId: AccountId;
    /**
     * The account ID of the hbar allowance owner.
     *
     * @readonly
     */
    readonly ownerAccountId: AccountId | null;
    /**
     * The current balance of the spender's allowance in tinybars.
     *
     * @readonly
     */
    readonly amount: Hbar;
    /**
     * @internal
     * @returns {proto.ICryptoAllowance}
     */
    _toProtobuf(): proto.ICryptoAllowance;
}
export namespace proto {
    type ICryptoAllowance = import("@hashgraph/proto").ICryptoAllowance;
    type IGrantedCryptoAllowance = import("@hashgraph/proto").IGrantedCryptoAllowance;
    type IAccountID = import("@hashgraph/proto").IAccountID;
}
export type Long = import("long");
import AccountId from "./AccountId.js";
import Hbar from "../Hbar.js";
