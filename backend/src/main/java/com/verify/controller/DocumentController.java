package com.verify.controller;

import com.verify.services.PinataService;
import com.verify.utils.AESUtil;

import com.verify.entity.Document;
import com.verify.repo.DocumentRepo;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.security.MessageDigest;
import java.util.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.net.URL;
import java.io.InputStream;

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

        @Autowired
        private PinataService pinataService;

        @Value("${blockchain.private-key}")
        private String PRIVATE_KEY;

        @Value("${blockchain.contract}")
        private String CONTRACT;

        @Value("${blockchain.rpc}")
        private String RPC;

        @Value("${AES_SECRET}")
        private String AES_SECRET;

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

        /*
         * =====================================================
         * ⭐ NEW HELPER (ONLY ADDITION)
         * Download raw bytes from IPFS
         * =====================================================
         */
        private byte[] downloadBytes(String url) throws Exception {
                URL u = new URL(url);
                try (InputStream in = u.openStream()) {
                        return in.readAllBytes();
                }
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

                var web3 = getWeb3();
                var receipt = web3.ethGetTransactionReceipt(txHash).send();

                while (receipt.getTransactionReceipt().isEmpty()) {
                        Thread.sleep(1500);
                        receipt = web3.ethGetTransactionReceipt(txHash).send();
                }
        }

        /* ================= REGISTER (UPLOAD) ================= */

        @PostMapping("/upload")
        public Map<String, Object> upload(@RequestParam("file") MultipartFile file)
                        throws Exception {

                if (file.getSize() > 5_000_000)
                        throw new RuntimeException("File too large");

                byte[] originalBytes = file.getBytes();

                String hash = sha256(originalBytes);

                if (repo.findByHash(hash).isPresent()) {
                        return Map.of("status", "DUPLICATE", "hash", hash);
                }

                byte[] encryptedBytes = AESUtil.encrypt(originalBytes, AES_SECRET);

                String cid = pinataService.uploadBytes(encryptedBytes);

                String fileUrl = "https://gateway.pinata.cloud/ipfs/" + cid;

                Document d = new Document();
                d.setName(file.getOriginalFilename());
                d.setHash(hash);
                d.setFileUrl(fileUrl);
                d.setEncryptionType("AES");
                d.setCreatedAt(Instant.now());

                repo.save(d);

                storeHashOnChain(hash);

                return Map.of(
                                "status", "REGISTERED",
                                "hash", hash,
                                "cid", cid,
                                "fileUrl", fileUrl,
                                "id", d.getId());
        }
        /* ================= BLOCKCHAIN VERIFY (MISSING METHOD FIX) ================= */

        private boolean existsOnChain(String hash) throws Exception {

                Function function = new Function(
                                "isValid",
                                List.of(new Utf8String(hash)),
                                List.of(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Bool>() {
                                }));

                String encoded = FunctionEncoder.encode(function);

                var response = getWeb3().ethCall(
                                org.web3j.protocol.core.methods.request.Transaction
                                                .createEthCallTransaction(null, CONTRACT, encoded),
                                org.web3j.protocol.core.DefaultBlockParameterName.LATEST)
                                .send();

                var decoded = org.web3j.abi.FunctionReturnDecoder.decode(
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

                if (dbMatch && chainMatch) {

                        Document d = repo.findByHash(uploadedHash).get();

                        d.setVerifiedAt(Instant.now());
                        d.setVerificationCount(d.getVerificationCount() + 1);
                        repo.save(d);

                        // ⭐ return secure backend download instead of raw CID
                        return Map.of(
                                        "status", "VERIFIED",
                                        "verifiedAt", d.getVerifiedAt().toString(),
                                        "fileUrl", "/download/" + d.getId());
                }

                if (dbMatch && !chainMatch)
                        return Map.of("status", "TAMPERED_DB");

                if (!dbMatch && chainMatch)
                        return Map.of("status", "BLOCKCHAIN_ONLY");

                return Map.of("status", "NOT_REGISTERED");
        }

        /*
         * =====================================================
         * ⭐ NEW ENDPOINT (ONLY ADDITION)
         * Secure download + auto decrypt
         * =====================================================
         */
        @GetMapping("/download/{id}")
        public ResponseEntity<byte[]> download(@PathVariable String id) throws Exception {

                Document d = repo.findById(id)
                                .orElseThrow(() -> new RuntimeException("File not found"));

                byte[] ipfsBytes = downloadBytes(d.getFileUrl());

                byte[] finalBytes;

                if ("AES".equals(d.getEncryptionType())) {
                        finalBytes = AESUtil.decrypt(ipfsBytes, AES_SECRET);
                } else {
                        finalBytes = ipfsBytes;
                }

                return ResponseEntity.ok()
                                .header("Content-Disposition",
                                                "attachment; filename=\"" + d.getName() + "\"")
                                .body(finalBytes);
        }

        /* ================= LIST ================= */

        @GetMapping("/docs")
        public List<Map<String, Object>> docs() {

                ZoneId ist = ZoneId.of("Asia/Kolkata");
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a 'IST'");

                List<Map<String, Object>> result = new ArrayList<>();

                for (Document d : repo.findAll()) {

                        String created = d.getCreatedAt() == null
                                        ? "—"
                                        : d.getCreatedAt().atZone(ist).format(fmt);

                        String verified = d.getVerifiedAt() == null
                                        ? "—"
                                        : d.getVerifiedAt().atZone(ist).format(fmt);

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
