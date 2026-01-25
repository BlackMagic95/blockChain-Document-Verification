// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

contract Verification {

    mapping(string => string) public records;

    function storeHash(string memory id, string memory hash) public {
        records[id] = hash;
    }

    function getHash(string memory id) public view returns(string memory){
        return records[id];
    }
}
