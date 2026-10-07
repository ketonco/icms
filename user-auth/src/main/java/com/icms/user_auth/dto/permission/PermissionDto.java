package com.icms.user_auth.dto.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import com.icms.shared.dto.IdentifiableDtoImpl;
public record PermissionDto(
    Long id,
    // must be at least 2 characters long, all uppercase letters, e.g., READ, WRITE, DELETE
    @NotBlank(message = "{perm.code.blank}")
    @Pattern (regexp = "^[A-Z]{2,}$", message = "{perm.code.pattern}")
    String code,
    @NotBlank(message = "{perm.name.blank}")
    String name,
    @NotNull (message = "{perm.active.null}")
    Boolean active
) implements IdentifiableDtoImpl<Long> {

}
