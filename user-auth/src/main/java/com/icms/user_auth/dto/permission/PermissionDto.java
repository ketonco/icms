package com.icms.user_auth.dto.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PermissionDto(
    Long id,
    // must be at least 2 characters long, all uppercase letters, e.g., READ, WRITE, DELETE
    @NotBlank(message = "Code must not be blank")
    @Pattern (regexp = "^[A-Z]{2,}$", message = "Code must be at least 2 uppercase letters")
    String code,
    @NotBlank(message = "Name must not be blank")
    String name,
    @NotNull (message = "Active cannot be null")
    Boolean active
) {

}
