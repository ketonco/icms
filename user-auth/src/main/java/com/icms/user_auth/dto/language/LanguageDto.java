package com.icms.user_auth.dto.language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import com.icms.shared.dto.IdentifiableDtoImpl;

public record LanguageDto(
    Long id,
    @NotBlank(message = "{lang.code.blank}")
    @Size(min = 2, max = 10, message = "{lang.code.size}")
    @Pattern(regexp = "^[a-z]{2,3}(-[A-Z]{2})?$", message = "{lang.code.pattern}")
    String code,
    @NotBlank(message = "{lang.name.blank}")
    @Size(min = 1, max = 100, message = "{lang.name.size}")
    String name,
    @NotNull(message = "{lang.isdefault.null}")
    Boolean isDefault,
    @NotNull(message = "{lang.active.null}")
    Boolean active
) implements IdentifiableDtoImpl<Long> {

}
