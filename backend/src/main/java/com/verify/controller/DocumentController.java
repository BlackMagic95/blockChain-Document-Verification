package com.verify.controller;

import com.verify.services.PinataService;

import com.verify.entity.Document;
import com.verify.repo.DocumentRepo;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

// import com.cloudinary.Cloudinary;
// import com.cloudinary.utils.ObjectUtils;

import java.security.MessageDigest;
import java.util.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.crypto.Credentials;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

@RestController
@CrossOrigin("*")

@RequestMapping("/")
public class DocumentController {

        @Autowired
        private DocumentRepo repo;

        // @Autowired
        // private Cloudinary cloudinary;

        @Autowired
        private PinataService pinataService;

        @Value("${blockchain.private-key}")
        private String PRIVATE_KEY;

        @Value("${blockchain.contract}")
        private String CONTRACT;

        @Value("${blockchain.rpc}")
        private String RPC;

        /* ================= WEB3 ================= */
        private Web3j getWeb3() {
                return Web3j.build(new HttpService(RPC));
        }

        /* ================= SHA-256 ================= */
        private String sha256(byte[] data) throws Exception {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(data);

                StringBuilder sb = new StringBuilder();
                for (byte b : hash)
                        sb.append(String.format("%02x", b));

                return sb.toString();
        }

        /* ================= BLOCKCHAIN ================= */
        private void storeHashOnChain(String hash) throws Exception {

                Credentials cred = Credentials.create(PRIVATE_KEY);

                RawTransactionManager tm = new RawTransactionManager(getWeb3(), cred, 11155111);

                Function function = new Function(
                                "storeHash",
                                List.of(new Utf8String(hash)),
                                List.of());

                String encoded = FunctionEncoder.encode(function);

                EthSendTransaction tx = tm.sendTransaction(
                                DefaultGasProvider.GAS_PRICE,
                                DefaultGasProvider.GAS_LIMIT,
                                CONTRACT,
                                encoded,
                                java.math.BigInteger.ZERO);

                String txHash = tx.getTransactionHash();

                System.out.println("Blockchain TX: " + txHash);
                System.out.println("Waiting for mining... ⏳");

                /* ⭐ WAIT UNTIL MINED (CRITICAL FIX) */
                var web3 = getWeb3();

                var receipt = web3.ethGetTransactionReceipt(txHash).send();

                while (receipt.getTransactionReceipt().isEmpty()) {
                        Thread.sleep(1500);
                        receipt = web3.ethGetTransactionReceipt(txHash).send();
                }

                System.out.println("TX MINED ✅");
        }

        /* ================= REGISTER ================= */
        @PostMapping("/upload")
        public Map<String, Object> upload(@RequestParam("file") MultipartFile file)
                        throws Exception {

                if (file.getSize() > 5_000_000)
                        throw new RuntimeException("File too large");

                byte[] bytes = file.getBytes();
                String hash = sha256(bytes);
                System.out.println("UPLOAD HASH: " + hash);

                /* Duplicate check */
                if (repo.findByHash(hash).isPresent()) {
                        return Map.of(
                                        "status", "DUPLICATE",
                                        "hash", hash);
                }

                /* ===== PINATA UPLOAD ===== */
                String cid = pinataService.upload(file);

                String fileUrl = "https://gateway.pinata.cloud/ipfs/" + cid;

                Document d = new Document();
                d.setName(file.getOriginalFilename());
                d.setHash(hash);
                d.setFileUrl(fileUrl); // still works same way
                d.setCreatedAt(Instant.now());

                repo.save(d);

                storeHashOnChain(hash);
                System.out.println("STORE CONTRACT: " + CONTRACT);

                return Map.of(
                                "status", "REGISTERED",
                                "hash", hash,
                                "cid", cid,
                                "fileUrl", fileUrl,
                                "id", d.getId());
        }

        /* ================= BLOCKCHAIN VERIFY ================= */
        private boolean existsOnChain(String hash) throws Exception {

                Function function = new Function(
                                "isValid", // ⭐ FIXED NAME
                                List.of(new Utf8String(hash)),
                                List.of(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Bool>() {
                                }));
                System.out.println("VERIFY CONTRACT: " + CONTRACT);

                String encoded = FunctionEncoder.encode(function);

                var response = getWeb3().ethCall(
                                org.web3j.protocol.core.methods.request.Transaction
                                                .createEthCallTransaction(null, CONTRACT, encoded),
                                org.web3j.protocol.core.DefaultBlockParameterName.LATEST)
                                .send();

                List<org.web3j.abi.datatypes.Type> decoded = org.web3j.abi.FunctionReturnDecoder.decode(
                                response.getValue(),
                                function.getOutputParameters());

                if (decoded.isEmpty())
                        return false;

                return (Boolean) decoded.get(0).getValue();
        }

        /* ================= VERIFY ================= */
        @PostMapping("/verify")
        public Map<String, String> verify(@RequestParam("file") MultipartFile file)
                        throws Exception {

                String uploadedHash = sha256(file.getBytes());

                boolean dbMatch = repo.findByHash(uploadedHash).isPresent();
                boolean chainMatch = existsOnChain(uploadedHash);
                System.out.println("VERIFY HASH: " + uploadedHash);

                /* ===== BOTH MATCH (ideal) ===== */
                if (dbMatch && chainMatch) {

                        Document d = repo.findByHash(uploadedHash).get();

                        d.setVerifiedAt(Instant.now());
                        d.setVerificationCount(d.getVerificationCount() + 1);
                        repo.save(d);

                        return Map.of(
                                        "status", "VERIFIED",
                                        "verifiedAt", d.getVerifiedAt().toString(),
                                        "fileUrl", d.getFileUrl());
                }

                /* ===== DB ONLY (tampered blockchain) ===== */
                if (dbMatch && !chainMatch) {
                        return Map.of(
                                        "status", "TAMPERED_DB",
                                        "message", "Found in DB but missing on blockchain");
                }

                /* ===== CHAIN ONLY (DB lost but safe) ===== */
                if (!dbMatch && chainMatch) {
                        return Map.of(
                                        "status", "BLOCKCHAIN_ONLY",
                                        "message", "Exists on blockchain but not DB");
                }

                /* ===== NOT FOUND ===== */
                return Map.of(
                                "status", "NOT_REGISTERED");
        }

        /* ================= LIST ================= */
        @GetMapping("/docs")
        public List<Map<String, Object>> docs() {

                ZoneId ist = ZoneId.of("Asia/Kolkata");

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a 'IST'");

                List<Map<String, Object>> result = new ArrayList<>();

                for (Document d : repo.findAll()) {

                        String created = "—";
                        if (d.getCreatedAt() != null) {
                                created = d.getCreatedAt() // Instant
                                                .atZone(ist) // ✅ ONLY this works
                                                .format(fmt);
                        }

                        String verified = "—";
                        if (d.getVerifiedAt() != null) {
                                verified = d.getVerifiedAt() // Instant
                                                .atZone(ist) // ✅ ONLY this works
                                                .format(fmt);
                        }

                        result.add(Map.of(
                                        "id", d.getId(),
                                        "name", d.getName(),
                                        "hash", d.getHash(),
                                        "fileUrl", d.getFileUrl(),
                                        "createdAt", created,
                                        "verifiedAt", verified));
                }

                return result;
        }

        /* ================= STATS ================= */
        @GetMapping("/stats")
        public Map<String, Object> stats() {

                long totalDocs = repo.count();

                long totalVerifications = repo.findAll()
                                .stream()
                                .mapToLong(Document::getVerificationCount)
                                .sum();

                return Map.of(
                                "totalDocs", totalDocs,
                                "totalVerifications", totalVerifications);
        }
}