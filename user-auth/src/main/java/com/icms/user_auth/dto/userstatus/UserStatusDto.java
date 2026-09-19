package com.icms.user_auth.dto.userstatus;
import com.icms.shared.dto.IdentifiableDtoImpl;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserStatusDto(
    Long id,
    @NotBlank(message = "Code must not be blank")
    // must be 3 UPPERCASE letters Ej: ACT, INA, SUS
    @Pattern(regexp = "^[A-Z]{3}$", message = "Code must be 3 uppercase letters")
    String code,
    @NotBlank(message = "Name must not be blank")
    String name,
    @NotNull (message = "Active cannot be null")
    Boolean active
) implements IdentifiableDtoImpl<Long> {
    
}
