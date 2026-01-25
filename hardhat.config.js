require("@nomiclabs/hardhat-ethers");

module.exports = {
  solidity: "0.8.20",
  networks: {
    sepolia: {
      url: "https://ethereum-sepolia-rpc.publicnode.com",
      accounts: ["0x209aeba85c22705e0f2029d2eede9f475825aa67221e8125a61a16c16a72cfca"]
    }
  }
};
