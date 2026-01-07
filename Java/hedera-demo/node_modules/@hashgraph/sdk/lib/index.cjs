"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
var _exportNames = {
  LocalProvider: true,
  LocalWallet: true,
  Client: true
};
Object.defineProperty(exports, "Client", {
  enumerable: true,
  get: function () {
    return _NodeClient.default;
  }
});
Object.defineProperty(exports, "LocalProvider", {
  enumerable: true,
  get: function () {
    return _LocalProvider.default;
  }
});
Object.defineProperty(exports, "LocalWallet", {
  enumerable: true,
  get: function () {
    return _LocalWallet.default;
  }
});

var _exports = require("./exports.cjs");

Object.keys(_exports).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _exports[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _exports[key];
    }
  });
});

var _LocalProvider = _interopRequireDefault(require("./LocalProvider.cjs"));

var _LocalWallet = _interopRequireDefault(require("./LocalWallet.cjs"));

var _NodeClient = _interopRequireDefault(require("./client/NodeClient.cjs"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }