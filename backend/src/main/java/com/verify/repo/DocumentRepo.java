package com.verify.repo;

import com.verify.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepo extends JpaRepository<Document, Long> {
    Optional<Document> findByHash(String hash);
}
