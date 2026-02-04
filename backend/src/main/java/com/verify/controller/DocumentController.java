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
import org.web3j.crypto.Credentials;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.Type;

import org.web3j.protocol.core.methods.response.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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

        byte[] bytes = file.getBytes();
        String hash = sha256(bytes);

        /* 🔥 DUPLICATE CHECK */
        if (repo.findByHash(hash).isPresent()) {
            return Map.of(
                    "status", "DUPLICATE",
                    "hash", hash);
        }

        /* 🔥 Upload to Cloudinary */
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                bytes,
                ObjectUtils.asMap("resource_type", "auto"));

        String fileUrl = uploadResult.get("secure_url").toString();

        /* 🔥 Save to Mongo */
        Document d = new Document();
        d.setName(file.getOriginalFilename());
        d.setHash(hash);
        d.setFileUrl(fileUrl);

        /* ⭐ REGISTER TIME FIX */
        d.setCreatedAt(LocalDateTime.now());

        repo.save(d);

        /* 🔥 Store on blockchain AFTER DB save */
        storeHashOnChain(hash);

        return Map.of(
                "status", "REGISTERED",
                "hash", hash,
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

        /* ⭐ VERIFY TIME */
        Document d = docOpt.get();
        d.setVerifiedAt(LocalDateTime.now());

        repo.save(d);

        ZoneId ist = ZoneId.of("Asia/Kolkata");

        String formattedTime = d.getVerifiedAt()
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ist)
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a 'IST'"));

        return Map.of(
                "status", "VERIFIED",
                "verifiedAt", formattedTime);
    }

    /* ================= LIST ================= */
    @GetMapping("/docs")
    public List<Map<String, Object>> docs() {

        ZoneId ist = ZoneId.of("Asia/Kolkata");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a 'IST'");

        List<Map<String, Object>> result = new ArrayList<>();

        for (Document d : repo.findAll(
                org.springframework.data.domain.Sort.by(
                        org.springframework.data.domain.Sort.Direction.DESC,
                        "createdAt"))) {

            String created = d.getCreatedAt() == null ? "—"
                    : d.getCreatedAt()
                            .atZone(ZoneId.systemDefault())
                            .withZoneSameInstant(ist)
                            .format(fmt);

            String verified = d.getVerifiedAt() == null ? "—"
                    : d.getVerifiedAt()
                            .atZone(ZoneId.systemDefault())
                            .withZoneSameInstant(ist)
                            .format(fmt);

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
        long verifiedDocs = repo.countByVerifiedAtIsNotNull();

        return Map.of(
                "totalDocs", totalDocs,
                "verifiedDocs", verifiedDocs);
    }
}
