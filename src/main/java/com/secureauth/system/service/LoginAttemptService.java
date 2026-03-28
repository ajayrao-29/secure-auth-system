package com.secureauth.system.service;

import com.secureauth.system.entity.User;
import com.secureauth.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final UserRepository userRepository;

    @Transactional
    public void loginSucceeded(User user) {
        if (user.getFailedAttempts() == 0 && !user.isAccountLocked()) {
            return;
        }

        user.setFailedAttempts(0);
        user.setAccountLocked(false);
        user.setLockTime(null);
        userRepository.save(user);
    }

    @Transactional
    public void loginFailed(User user) {
        int failedAttempts = user.getFailedAttempts() + 1;
        user.setFailedAttempts(failedAttempts);

        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            user.setAccountLocked(true);
            user.setLockTime(Instant.now());
        }

        userRepository.save(user);
    }
}
