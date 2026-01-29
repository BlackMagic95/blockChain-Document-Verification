package com.verify.repo;

import com.verify.entity.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DocumentRepo extends MongoRepository<Document, String> {

    Optional<Document> findByHash(String hash);
}
