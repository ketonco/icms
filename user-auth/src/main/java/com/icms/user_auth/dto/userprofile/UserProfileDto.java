package com.icms.user_auth.dto.userprofile;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.icms.shared.dto.IdentifiableDtoImpl;

import java.util.Map;

    public record UserProfileDto(
    UUID id,
    @NotBlank(message = "First name must not be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    String firstName,
    @NotBlank(message = "Last name must not be blank")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    String lastName,
    String avatarUrl,   
    Map<String, Object> contact,   
    Map<String, Object> prefs,
    @NotBlank(message = "Email must not be blank")
    @Size(max = 100, message = "Email must be at most 100 characters")
    String email
) implements IdentifiableDtoImpl<UUID> {

}
