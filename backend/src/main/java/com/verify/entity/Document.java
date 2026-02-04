package com.verify.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@org.springframework.data.mongodb.core.mapping.Document(collection = "document")
public class Document {

    @Id
    private String id;

    private String name;

    /* ⭐ prevents duplicates at DB level */
    @Indexed(unique = true)
    private String hash;

    private String fileUrl;

    /* ⭐ REGISTER TIME (NEW) */
    private LocalDateTime createdAt;

    /* ⭐ VERIFY TIME */
    private LocalDateTime verifiedAt;

    /* ================= GETTERS & SETTERS ================= */

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }
}
