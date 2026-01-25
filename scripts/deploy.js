async function main() {

  const Contract = await ethers.getContractFactory("Verification");

  const contract = await Contract.deploy();

  await contract.deployed();

  console.log("✅ Contract deployed to:", contract.address);
}

main().catch(console.error);
