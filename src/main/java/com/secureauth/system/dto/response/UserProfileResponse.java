package com.secureauth.system.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final String role;
    private final boolean accountLocked;
    private final int failedAttempts;
}
