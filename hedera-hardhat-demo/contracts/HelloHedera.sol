// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

contract HelloHedera {
    string private message;

    constructor(string memory initMessage) {
        message = initMessage;
    }

    function getMessage() public view returns (string memory) {
        return message;
    }

    function setMessage(string memory newMessage) public {
        message = newMessage;
    }
}
