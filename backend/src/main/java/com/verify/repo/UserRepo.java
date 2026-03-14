package com.verify.repo;

import com.verify.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.List;

public interface UserRepo extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    List<User> findByApprovalStatusOrderByRequestedAtDesc(String approvalStatus);
}