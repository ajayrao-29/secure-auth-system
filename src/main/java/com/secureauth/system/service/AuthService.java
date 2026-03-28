package com.secureauth.system.service;

import com.secureauth.system.dto.request.LoginRequest;
import com.secureauth.system.dto.request.RefreshTokenRequest;
import com.secureauth.system.dto.request.RegisterRequest;
import com.secureauth.system.dto.response.AuthResponse;
import com.secureauth.system.entity.Role;
import com.secureauth.system.entity.User;
import com.secureauth.system.exception.AccountLockedException;
import com.secureauth.system.exception.ResourceAlreadyExistsException;
import com.secureauth.system.repository.UserRepository;
import com.secureauth.system.util.InputSanitizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final LoginAttemptService loginAttemptService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String username = InputSanitizer.sanitize(request.getUsername());
        String email = InputSanitizer.sanitizeEmail(request.getEmail());

        if (userRepository.existsByUsername(username)) {
            throw new ResourceAlreadyExistsException("Username is already in use");
        }
        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email is already in use");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .failedAttempts(0)
                .accountLocked(false)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());
        return buildAuthResponse(savedUser);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String username = InputSanitizer.sanitize(request.getUsername());
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (user.isAccountLocked()) {
            throw new AccountLockedException("Account is locked due to too many failed login attempts");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword()));
            loginAttemptService.loginSucceeded(user);
            log.info("User logged in successfully: {}", username);
            return buildAuthResponse(user);
        } catch (BadCredentialsException ex) {
            loginAttemptService.loginFailed(user);
            log.warn("Failed login attempt for user: {}", username);
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        User user = refreshTokenService.verifyAndGetUser(request.getRefreshToken());
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpirationSeconds())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }
}
