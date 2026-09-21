package com.icms.user_auth.dto.user;

import com.icms.shared.dto.IdentifiableDtoImpl;
import com.icms.user_auth.dto.userprofile.UserProfileDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserDto(
    UUID id,
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") 
    String username,
    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    @Size(max = 100, message = "Email must be at most 100 characters")
    String email,
    boolean enabled,
    boolean locked,
    int failedLoginAttempts,
    LocalDateTime lastLoginAttempt,
    String status,
    Set<String> roles,
    UserProfileDto userProfile
) implements IdentifiableDtoImpl<UUID>{

}
