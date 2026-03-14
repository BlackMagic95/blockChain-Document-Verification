package com.verify.repo;

import com.verify.entity.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepo extends MongoRepository<Document, String> {

    // Original — unchanged
    Optional<Document> findByHash(String hash);

    long countByVerifiedAtIsNotNull();

    // ⭐ NEW — college-scoped queries
    List<Document> findByCollegeId(String collegeId);

    long countByCollegeId(String collegeId);

    long countByCollegeIdAndVerifiedAtIsNotNull(String collegeId);
}