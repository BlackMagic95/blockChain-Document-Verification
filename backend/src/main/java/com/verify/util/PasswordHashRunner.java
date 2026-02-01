package com.verify.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordHashRunner {

    @Bean
    CommandLineRunner hashRunner() {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            String raw = "admin123"; // change if needed
            String hash = encoder.encode(raw);

            System.out.println("================================");
            System.out.println("RAW PASSWORD : " + raw);
            System.out.println("BCrypt HASH  : " + hash);
            System.out.println("================================");
        };
    }
}
