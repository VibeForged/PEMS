package com.example.pems.config;

import com.example.pems.entity.User;
import com.example.pems.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrapConfig {

    @Bean
    ApplicationRunner bootstrapAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${admin.name:}") String adminName,
            @Value("${admin.email:}") String adminEmail,
            @Value("${admin.password:}") String adminPassword) {
        return args -> {
            if (adminEmail.isBlank()) {
                return;
            }
            if (adminName.isBlank() || adminPassword.length() < 6) {
                throw new IllegalStateException(
                        "ADMIN_NAME and ADMIN_PASSWORD (6+ characters) are required when ADMIN_EMAIL is set");
            }

            User admin = userRepository.findByEmail(adminEmail).orElseGet(() -> {
                User created = new User(adminName, adminEmail, passwordEncoder.encode(adminPassword));
                created.setRole(User.Role.ADMIN);
                created.setActive(true);
                return created;
            });
            admin.setRole(User.Role.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);
        };
    }
}
