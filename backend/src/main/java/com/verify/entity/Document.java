package com.verify.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents", uniqueConstraints = @UniqueConstraint(columnNames = "hash"))
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 64)
    private String hash;

    @Column(nullable = false, length = 500)
    private String fileUrl;

    private LocalDateTime registeredAt = LocalDateTime.now();

    private LocalDateTime verifiedAt;

    /* ===== GETTERS / SETTERS ===== */

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHash() {
        return hash;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }
}
