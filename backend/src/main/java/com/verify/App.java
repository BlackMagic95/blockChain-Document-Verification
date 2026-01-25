package com.verify;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.persistence.*;

import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.crypto.*;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.*;

@SpringBootApplication
@EnableJpaRepositories(considerNestedRepositories = true)
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    // ================= ENTITY =================
    @Entity
    static class Document {

        @Id
        @GeneratedValue
        public Long id;

        public String name;
        public String path;
        public String hash;
    }

    interface DocumentRepo extends JpaRepository<Document, Long> {
    }

    // ================= HASH =================
    static String sha256(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(data);

        StringBuilder sb = new StringBuilder();
        for (byte b : hash)
            sb.append(String.format("%02x", b));

        return sb.toString();
    }

    // ================= CONTROLLER =================
    @RestController
    @CrossOrigin("*")
    static class DocumentController {

        @Autowired
        DocumentRepo repo;

        private final String RPC = "https://ethereum-sepolia-rpc.publicnode.com";
        private final String PRIVATE_KEY = "0x209aeba85c22705e0f2029d2eede9f475825aa67221e8125a61a16c16a72cfca";
        private final String CONTRACT = "0x5B599CcB905E9E4D171FFC363A0E8d560aE4C10B";

        Web3j web3 = Web3j.build(new HttpService(RPC));

        // ================= STORE ON CHAIN =================
        private void storeHashOnChain(String id, String hash) throws Exception {

            Credentials cred = Credentials.create(PRIVATE_KEY);
            RawTransactionManager tm = new RawTransactionManager(web3, cred, 11155111);

            Function function = new Function(
                    "storeHash",
                    Arrays.asList(new Utf8String(id), new Utf8String(hash)),
                    Collections.emptyList());

            String encoded = FunctionEncoder.encode(function);

            var response = tm.sendTransaction(
                    DefaultGasProvider.GAS_PRICE,
                    DefaultGasProvider.GAS_LIMIT,
                    CONTRACT,
                    encoded,
                    java.math.BigInteger.ZERO);

            if (response.hasError())
                System.out.println("ERROR: " + response.getError().getMessage());
            else
                System.out.println("Blockchain TX: " + response.getTransactionHash());
        }

        // ================= UPLOAD =================
        @PostMapping("/upload")
        public Document upload(@RequestParam("file") MultipartFile file) throws Exception {

            Files.createDirectories(Paths.get("uploads"));

            Path p = Paths.get("uploads/" + file.getOriginalFilename());
            Files.write(p, file.getBytes());

            String hash = sha256(file.getBytes());

            Document d = new Document();
            d.name = file.getOriginalFilename();
            d.path = p.toString();
            d.hash = hash;

            repo.save(d);

            storeHashOnChain(d.id.toString(), hash);

            return d;
        }

        // ================= VERIFY BY HASH (🔥 NEW) =================
        @PostMapping("/verifyFile")
        public String verifyFile(@RequestParam("file") MultipartFile file) throws Exception {

            String newHash = sha256(file.getBytes());

            List<Document> docs = repo.findAll();

            for (Document d : docs) {
                if (d.hash.equals(newHash)) {
                    return "VERIFIED ✅";
                }
            }

            return "TAMPERED ❌";
        }

        // ================= VERIFY BY BLOCKCHAIN =================
        @GetMapping("/verify/{id}")
        public String verify(@PathVariable String id) throws Exception {

            Credentials cred = Credentials.create(PRIVATE_KEY);

            Function function = new Function(
                    "getHash",
                    Arrays.asList(new Utf8String(id)),
                    Arrays.asList(new TypeReference<Utf8String>() {
                    }));

            String encoded = FunctionEncoder.encode(function);

            var response = web3.ethCall(
                    org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(
                            cred.getAddress(), CONTRACT, encoded),
                    org.web3j.protocol.core.DefaultBlockParameterName.LATEST).send();

            List<Type> outputs = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());

            String chainHash = outputs.get(0).getValue().toString();

            Document d = repo.findById(Long.parseLong(id)).get();

            if (chainHash.equals(d.hash))
                return "VERIFIED";
            else
                return "TAMPERED";
        }

        // ================= LIST =================
        @GetMapping("/docs")
        public List<Document> all() {
            return repo.findAll();
        }

        // ================= DOWNLOAD =================
        @GetMapping("/download/{id}")
        public byte[] download(@PathVariable Long id) throws Exception {
            Document d = repo.findById(id).get();
            return Files.readAllBytes(Paths.get(d.path));
        }
    }
}
