package com.verify.controller;

import com.verify.entity.Document;
import com.verify.repo.DocumentRepo;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.crypto.*;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.abi.*;
import org.web3j.abi.datatypes.*;
import org.web3j.protocol.core.methods.response.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/")
public class DocumentController {

    @Autowired
    private DocumentRepo repo;

    @Autowired
    private Cloudinary cloudinary;

    @Value("${blockchain.private-key}")
    private String PRIVATE_KEY;

    @Value("${blockchain.contract}")
    private String CONTRACT;

    @Value("${blockchain.rpc}")
    private String RPC;

    /* ================= WEB3 CONNECTION ================= */
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
                List.of(new Utf8String(hash), new Utf8String(hash)),
                List.of());

        String encoded = FunctionEncoder.encode(function);

        EthSendTransaction tx = tm.sendTransaction(
                DefaultGasProvider.GAS_PRICE,
                DefaultGasProvider.GAS_LIMIT,
                CONTRACT,
                encoded,
                java.math.BigInteger.ZERO);

        System.out.println("Blockchain TX: " + tx.getTransactionHash());
    }

    /* ================= REGISTER ================= */
    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file)
            throws Exception {

        System.out.println("UPLOAD HIT"); // 🔥 debug

        String hash = sha256(file.getBytes());

        if (repo.findByHash(hash).isPresent()) {
            return Map.of("status", "DUPLICATE");
        }

        /* upload to cloudinary */
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto"));

        String fileUrl = uploadResult.get("secure_url").toString();

        Document d = new Document();
        d.setName(file.getOriginalFilename());
        d.setHash(hash);
        d.setFileUrl(fileUrl);

        repo.save(d);

        storeHashOnChain(hash);

        return Map.of(
                "status", "REGISTERED",
                "hash", hash, // ⭐ added
                "id", d.getId(),
                "fileUrl", fileUrl);
    }

    /* ================= VERIFY ================= */
    @PostMapping("/verify")
    public Map<String, String> verify(@RequestParam("file") MultipartFile file)
            throws Exception {

        String uploadedHash = sha256(file.getBytes());

        Optional<Document> docOpt = repo.findByHash(uploadedHash);

        if (docOpt.isEmpty()) {
            return Map.of("status", "NOT_REGISTERED");
        }

        Credentials cred = Credentials.create(PRIVATE_KEY);

        Function function = new Function(
                "getHash",
                List.of(new Utf8String(uploadedHash)),
                List.of(new TypeReference<Utf8String>() {
                }));

        String encoded = FunctionEncoder.encode(function);

        EthCall call = getWeb3().ethCall(
                org.web3j.protocol.core.methods.request.Transaction
                        .createEthCallTransaction(
                                cred.getAddress(),
                                CONTRACT,
                                encoded),
                org.web3j.protocol.core.DefaultBlockParameterName.LATEST).send();

        List<Type> decoded = FunctionReturnDecoder.decode(call.getValue(),
                function.getOutputParameters());

        if (decoded.isEmpty()) {
            return Map.of("status", "CHAIN_MISSING");
        }

        Document d = docOpt.get();
        d.setVerifiedAt(LocalDateTime.now());
        repo.save(d);

        return Map.of(
                "status", "VERIFIED",
                "verifiedAt", d.getVerifiedAt().toString());
    }

    /* ================= LIST ================= */
    @GetMapping("/docs")
    public List<Document> docs() {
        System.out.println("DOCS HIT");
        return repo.findAll();
    }

    /* ================= STATS ================= */
    @GetMapping("/stats")
    public Map<String, Object> stats() {

        long total = repo.count();

        long verified = repo.findAll()
                .stream()
                .filter(d -> d.getVerifiedAt() != null)
                .count();

        return Map.of(
                "totalDocs", total,
                "verifiedDocs", verified,
                "integrity", 100);
    }
}
