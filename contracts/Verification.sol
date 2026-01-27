mapping(string => bool) public registeredHashes;

function storeHash(string memory hash) public {
    registeredHashes[hash] = true;
}

function isValid(string memory hash) public view returns (bool) {
    return registeredHashes[hash];
}
