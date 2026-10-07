package com.icms.user_auth.dto.userprofile;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.icms.shared.dto.IdentifiableDtoImpl;

import java.util.Map;

    public record UserProfileDto(
    UUID id,
    @NotBlank(message = "{usrprof.firstname.blank}")
    @Size(min = 2, max = 50, message = "{usrprof.firstname.size}")
    String firstName,
    @NotBlank(message = "{usrprof.lastname.blank}")
    @Size(min = 2, max = 50, message = "{usrprof.lastname.size}")
    String lastName,
    String avatarUrl,   
    Map<String, Object> contact,   
    Map<String, Object> prefs,
    @NotBlank(message = "{usrprof.email.blank}")
    @Size(max = 100, message = "{usrprof.email.size}")
    String email
) implements IdentifiableDtoImpl<UUID> {

}
