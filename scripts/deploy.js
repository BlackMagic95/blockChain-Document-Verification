async function main() {

  const Contract = await ethers.getContractFactory("DocumentVerification");

  const contract = await Contract.deploy();

  await contract.deployed(); // ✅ v5 fix

  console.log("Deployed to:", contract.address);
}

main();
