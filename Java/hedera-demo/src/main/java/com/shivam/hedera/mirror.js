// node mirror.js 0.0.6438872
const { utils } = require("@hashgraph/hethers");

const account = process.argv[2] || "0.0.6438872";
const addr = utils.getAddressFromAccount(account);
console.log(addr);
