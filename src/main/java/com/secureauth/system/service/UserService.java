package com.secureauth.system.service;

import com.secureauth.system.dto.response.UserProfileResponse;
import com.secureauth.system.entity.User;
import com.secureauth.system.exception.ResourceNotFoundException;
import com.secureauth.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .accountLocked(user.isAccountLocked())
                .failedAttempts(user.getFailedAttempts())
                .build();
    }
}
