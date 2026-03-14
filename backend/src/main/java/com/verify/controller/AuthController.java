package com.verify.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.verify.entity.User;
import com.verify.repo.UserRepo;
import com.verify.security.JwtUtil;
import com.verify.security.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private static final String SUPER_ADMIN_EMAIL = "rohanrk2611@gmail.com";

    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;

    @Value("${google.client-id}")
    private String googleClientId;

    public AuthController(JwtUtil jwtUtil, UserRepo userRepo) {
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
    }

    @PostMapping("/google")
    public Map<String, String> googleLogin(@RequestBody Map<String, String> body) throws Exception {
        String token = body.get("token");

        if (token == null || token.isBlank()) {
            throw new RuntimeException("Google token missing");
        }

        String email = verifyAndExtractEmail(token);

        if (SUPER_ADMIN_EMAIL.equalsIgnoreCase(email)) {
            return issueAuthResponse(email, Role.SUPER_ADMIN);
        }

        Optional<User> maybeUser = userRepo.findByEmail(email);

        if (maybeUser.isEmpty() || !"APPROVED".equalsIgnoreCase(maybeUser.get().getApprovalStatus())) {
            return Map.of(
                    "status", "PENDING_APPROVAL",
                    "email", email,
                    "message", "Please submit college access request and wait for super admin approval.");
        }

        User user = maybeUser.get();
        String storedRole = user.getRole();
        if (storedRole == null || storedRole.isBlank() || "ADMIN".equalsIgnoreCase(storedRole)) {
            storedRole = Role.COLLEGE_ADMIN.name();
        }

        Role userRole = Role.valueOf(storedRole);
        return issueAuthResponse(email, userRole);
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

    private Map<String, String> issueAuthResponse(String email, Role role) {
        List<String> authorities = new ArrayList<>(
                role.getPermissions().stream().map(Enum::name).toList());
        authorities.add("ROLE_" + role.name());

        String jwt = jwtUtil.generateToken(email, role.name(), authorities);

        return Map.of(
                "status", "APPROVED",
                "token", jwt,
                "role", role.name(),
                "email", email);
    }
}