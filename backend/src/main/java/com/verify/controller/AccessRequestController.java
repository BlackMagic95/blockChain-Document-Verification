package com.verify.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.verify.entity.User;
import com.verify.repo.UserRepo;
import com.verify.security.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/access")
@CrossOrigin("*")
public class AccessRequestController {

    private static final String SUPER_ADMIN_EMAIL = "rohanrk2611@gmail.com";

    private final UserRepo userRepo;

    @Value("${google.client-id}")
    private String googleClientId;

    public AccessRequestController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/request")
    public Map<String, String> submitAccessRequest(@RequestBody Map<String, String> body) throws Exception {
        String token = body.get("token");
        String requestedEmail = body.getOrDefault("email", "").trim().toLowerCase();
        String collegeName = body.getOrDefault("collegeName", "").trim();
        String contactName = body.getOrDefault("contactName", "").trim();
        String contactPhone = body.getOrDefault("contactPhone", "").trim();

        if (collegeName.isBlank() || contactName.isBlank()) {
            throw new RuntimeException("College and contact person are required");
        }

        String email;
        if (token != null && !token.isBlank()) {
            email = verifyAndExtractEmail(token).toLowerCase();

            if (!requestedEmail.isBlank() && !requestedEmail.equals(email)) {
                throw new RuntimeException("Entered email does not match signed-in Google account");
            }
        } else {
            if (requestedEmail.isBlank()) {
                throw new RuntimeException("Email is required");
            }
            if (!requestedEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new RuntimeException("Invalid email format");
            }
            email = requestedEmail;
        }

        if (SUPER_ADMIN_EMAIL.equalsIgnoreCase(email)) {
            return Map.of("status", "APPROVED", "message", "Super admin already has access");
        }

        User user = userRepo.findByEmail(email).orElseGet(User::new);
        user.setEmail(email);
        user.setRole(Role.COLLEGE_ADMIN.name());
        user.setCollegeName(collegeName);
        user.setContactName(contactName);
        user.setContactPhone(contactPhone);

        if (!"APPROVED".equalsIgnoreCase(user.getApprovalStatus())) {
            user.setApprovalStatus("PENDING");
            user.setRequestedAt(Instant.now());
            user.setApprovedAt(null);
        }

        userRepo.save(user);

        return Map.of(
                "status", "SUBMITTED",
                "email", email,
                "message", "Request submitted. Wait for super admin approval.");
    }

    @GetMapping("/requests")
    @PreAuthorize("hasAuthority('ACCESS_APPROVE')")
    public List<Map<String, Object>> pendingRequests() {
        List<User> users = userRepo.findByApprovalStatusOrderByRequestedAtDesc("PENDING");
        List<Map<String, Object>> response = new ArrayList<>();

        for (User user : users) {
            response.add(Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "collegeName", Optional.ofNullable(user.getCollegeName()).orElse(""),
                    "contactName", Optional.ofNullable(user.getContactName()).orElse(""),
                    "contactPhone", Optional.ofNullable(user.getContactPhone()).orElse(""),
                    "requestedAt", Optional.ofNullable(user.getRequestedAt()).map(Instant::toString).orElse("-")));
        }

        return response;
    }

    @PostMapping("/requests/{id}/approve")
    @PreAuthorize("hasAuthority('ACCESS_APPROVE')")
    public Map<String, String> approveRequest(@PathVariable String id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));

        user.setApprovalStatus("APPROVED");
        user.setRole(Role.COLLEGE_ADMIN.name());
        user.setApprovedAt(Instant.now());
        userRepo.save(user);

        return Map.of("status", "APPROVED", "email", user.getEmail());
    }

    @PostMapping("/requests/{id}/reject")
    @PreAuthorize("hasAuthority('ACCESS_APPROVE')")
    public Map<String, String> rejectRequest(@PathVariable String id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));

        user.setApprovalStatus("REJECTED");
        userRepo.save(user);

        return Map.of("status", "REJECTED", "email", user.getEmail());
    }

    private String verifyAndExtractEmail(String token) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(token);
        if (idToken == null) {
            throw new RuntimeException("Invalid Google token");
        }

        return idToken.getPayload().getEmail();
    }
}