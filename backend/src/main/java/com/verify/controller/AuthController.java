package com.verify.controller;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.verify.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://blockchain-document-verification-1.onrender.com"
})

public class AuthController {

    private final JwtUtil jwtUtil;

    @Value("${google.client-id}")
    private String googleClientId;

    // 🔐 Only allowed admin emails
    private static final Set<String> ADMIN_EMAILS = Set.of(
            "rohanrk2611@gmail.com");

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/google")
    public Map<String, String> googleLogin(@RequestBody Map<String, String> body) throws Exception {

        String token = body.get("token");

        if (token == null || token.isBlank()) {
            throw new RuntimeException("Google token missing");
        }

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(token);

        if (idToken == null) {
            throw new RuntimeException("Invalid Google token");
        }

        String email = idToken.getPayload().getEmail();

        if (!ADMIN_EMAILS.contains(email)) {
            throw new RuntimeException("Not authorized as admin");
        }

        String jwt = jwtUtil.generateToken(email, "ADMIN");

        return Map.of(
                "token", jwt,
                "role", "ADMIN");
    }
}
