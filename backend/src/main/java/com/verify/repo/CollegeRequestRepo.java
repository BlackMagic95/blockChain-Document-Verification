package com.verify.repo;

import com.verify.entity.CollegeRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CollegeRequestRepo extends MongoRepository<CollegeRequest, String> {

    // AuthController uses this to check if a login email is approved
    Optional<CollegeRequest> findByAdminEmailAndStatus(String adminEmail, String status);

    // CollegeController uses this to list requests by status
    List<CollegeRequest> findByStatus(String status);

    // Guard against duplicate applications from same email
    boolean existsByAdminEmail(String adminEmail);
}