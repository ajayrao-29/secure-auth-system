package com.secureauth.system.config;

import com.secureauth.system.entity.Role;
import com.secureauth.system.entity.User;
import com.secureauth.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BootstrapAdminConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner bootstrapAdminRunner(
            @Value("${app.bootstrap-admin.enabled:false}") boolean enabled,
            @Value("${app.bootstrap-admin.username:}") String username,
            @Value("${app.bootstrap-admin.email:}") String email,
            @Value("${app.bootstrap-admin.password:}") String password) {
        return args -> {
            if (!enabled || username.isBlank() || email.isBlank() || password.isBlank()) {
                return;
            }

            if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
                log.info("Bootstrap admin already exists or conflicts with existing account");
                return;
            }

            User admin = User.builder()
                    .username(username.trim())
                    .email(email.trim().toLowerCase())
                    .password(passwordEncoder.encode(password))
                    .role(Role.ROLE_ADMIN)
                    .failedAttempts(0)
                    .accountLocked(false)
                    .build();

            userRepository.save(admin);
            log.info("Bootstrap admin user created: {}", admin.getUsername());
        };
    }
}
