// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

contract DocumentVerification {

    mapping(string => bool) public registeredHashes;

    /* ================= STORE ================= */
    function storeHash(string memory hash) public {
        registeredHashes[hash] = true;
    }

    /* ================= OLD (keep for compatibility) ================= */
    function isValid(string memory hash) public view returns (bool) {
        return registeredHashes[hash];
    }

    /* ================= NEW (for backend hybrid verify) ================= */
    function verifyHash(string memory hash) public view returns (bool) {
        return registeredHashes[hash];
    }
}
