/**
 * @namespace proto
 * @typedef {import("@hashgraph/proto").IAccountAmount} proto.IAccountAmount
 * @typedef {import("@hashgraph/proto").IAccountID} proto.IAccountID
 */
/**
 * @typedef {import("bignumber.js").default} BigNumber
 * @typedef {import("long")} Long
 */
/**
 * An account, and the amount that it sends or receives during a cryptocurrency transfer.
 */
export default class Transfer {
    /**
     * @internal
     * @param {proto.IAccountAmount[]} accountAmounts
     * @returns {Transfer[]}
     */
    static _fromProtobuf(accountAmounts: proto.IAccountAmount[]): Transfer[];
    /**
     * @internal
     * @param {object} props
     * @param {AccountId | string} props.accountId
     * @param {number | string | Long | BigNumber | Hbar} props.amount
     * @param {boolean} props.isApproved
     */
    constructor(props: {
        accountId: AccountId | string;
        amount: number | string | Long | BigNumber | Hbar;
        isApproved: boolean;
    });
    /**
     * The Account ID that sends or receives cryptocurrency.
     *
     * @readonly
     */
    readonly accountId: AccountId;
    /**
     * The amount of tinybars that the account sends(negative) or receives(positive).
     */
    amount: Hbar;
    isApproved: boolean;
    /**
     * @internal
     * @returns {proto.IAccountAmount}
     */
    _toProtobuf(): proto.IAccountAmount;
}
export namespace proto {
    type IAccountAmount = import("@hashgraph/proto").IAccountAmount;
    type IAccountID = import("@hashgraph/proto").IAccountID;
}
export type BigNumber = import("bignumber.js").default;
export type Long = import("long");
import AccountId from "./account/AccountId.js";
import Hbar from "./Hbar.js";
