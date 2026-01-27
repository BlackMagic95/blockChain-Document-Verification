package com.verify.controller;

import com.verify.entity.Document;
import com.verify.repo.DocumentRepo;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

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
public class DocumentController {

    @Autowired
    private DocumentRepo repo;

    private final String RPC = "https://ethereum-sepolia-rpc.publicnode.com";
    private final String PRIVATE_KEY = System.getenv("PRIVATE_KEY");
    private final String CONTRACT = "0x5B599CcB905E9E4D171FFC363A0E8d560aE4C10B";

    private final Web3j web3 = Web3j.build(new HttpService(RPC));

    public DocumentController() {
        if (PRIVATE_KEY == null || PRIVATE_KEY.isBlank()) {
            throw new RuntimeException("PRIVATE_KEY not set");
        }
    }

    // ================= SHA-256 =================
    private String sha256(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte b : hash)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    // ================= BLOCKCHAIN STORE =================
    private void storeHashOnChain(String hash) throws Exception {
        Credentials cred = Credentials.create(PRIVATE_KEY);
        RawTransactionManager tm = new RawTransactionManager(web3, cred, 11155111);

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

    // ================= REGISTER =================
    @PostMapping("/upload")
    public Map<String, String> upload(@RequestParam MultipartFile file)
            throws Exception {

        String hash = sha256(file.getBytes());

        // 1️⃣ DUPLICATE CHECK
        if (repo.findByHash(hash).isPresent()) {
            return Map.of(
                    "status", "DUPLICATE",
                    "message", "Document already registered");
        }

        Document d = new Document();
        d.setName(file.getOriginalFilename());
        d.setHash(hash);
        repo.save(d);

        storeHashOnChain(hash);

        return Map.of(
                "status", "REGISTERED",
                "hash", hash);
    }

    // ================= VERIFY =================
    @PostMapping("/verify")
    public Map<String, String> verify(@RequestParam MultipartFile file)
            throws Exception {

        String uploadedHash = sha256(file.getBytes());

        Optional<Document> docOpt = repo.findByHash(uploadedHash);

        if (docOpt.isEmpty()) {
            return Map.of(
                    "status", "NOT_REGISTERED",
                    "message", "Document not found");
        }

        // 2️⃣ READ FROM BLOCKCHAIN
        Function function = new Function(
                "getHash",
                List.of(new Utf8String(uploadedHash)),
                List.of(new TypeReference<Utf8String>() {
                }));

        String encoded = FunctionEncoder.encode(function);

        EthCall call = web3.ethCall(
                org.web3j.protocol.core.methods.request.Transaction
                        .createEthCallTransaction(
                                Credentials.create(PRIVATE_KEY).getAddress(),
                                CONTRACT,
                                encoded),
                org.web3j.protocol.core.DefaultBlockParameterName.LATEST).send();

        List<Type> decoded = FunctionReturnDecoder.decode(
                call.getValue(),
                function.getOutputParameters());

        if (decoded.isEmpty()) {
            return Map.of(
                    "status", "CHAIN_MISSING",
                    "message", "Hash not found on blockchain");
        }

        // 3️⃣ VERIFIED + TIMESTAMP
        Document d = docOpt.get();
        d.setVerifiedAt(LocalDateTime.now());
        repo.save(d);

        return Map.of(
                "status", "VERIFIED",
                "verifiedAt", d.getVerifiedAt().toString());
    }

    // ================= LIST =================
    @GetMapping("/docs")
    public List<Document> docs() {
        return repo.findAll();
    }
}
