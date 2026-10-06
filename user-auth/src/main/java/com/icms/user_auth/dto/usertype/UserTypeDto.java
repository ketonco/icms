package com.icms.user_auth.dto.usertype;
import com.icms.shared.dto.IdentifiableDtoImpl;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserTypeDto(
    Long id,
    @NotBlank(message = "{usrtype.code.blank}")
    // must be 3 UPPERCASE letters Ej: ADM, USR, MOD, CLI
    @Pattern(regexp = "^[A-Z]{3}$", message = "{usrtype.code.pattern}")
    String code,
    @NotBlank(message = "{usrtype.name.blank}")
    String name,
    @NotNull(message = "{usrtype.active.null}")
    Boolean active
) implements IdentifiableDtoImpl<Long> {

}
