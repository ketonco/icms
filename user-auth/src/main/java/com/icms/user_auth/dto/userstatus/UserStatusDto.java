package com.icms.user_auth.dto.userstatus;
import com.icms.shared.dto.IdentifiableDtoImpl;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserStatusDto(
    Long id,
    @NotBlank(message = "{usrstatus.code.blank}")
    // must be 3 UPPERCASE letters Ej: ACT, INA, SUS
    @Pattern(regexp = "^[A-Z]{3}$", message = "{usrstatus.code.pattern}")
    String code,
    @NotBlank(message = "{usrstatus.name.blank}")
    String name,
    @NotNull(message = "{usrstatus.active.null}")
    Boolean active
) implements IdentifiableDtoImpl<Long> {
    
}
