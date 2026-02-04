package com.verify.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

@org.springframework.data.mongodb.core.mapping.Document(collection = "document")
public class Document {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String hash;

    private String fileUrl;

    // ✅ Mongo-safe dates
    private Instant createdAt;
    private Instant verifiedAt;

    // ✅ counter
    private long verificationCount = 0;

    /* getters setters */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Instant verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public long getVerificationCount() {
        return verificationCount;
    }

    public void setVerificationCount(long verificationCount) {
        this.verificationCount = verificationCount;
    }
}
