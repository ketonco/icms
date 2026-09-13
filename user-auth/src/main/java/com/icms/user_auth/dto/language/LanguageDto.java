package com.icms.user_auth.dto.language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record LanguageDto(
    Long id,
    @NotBlank(message = "Code cannot be blank")
    @Size(min = 2, max = 10, message = "Code must be between 2 and 10 characters")
    @Pattern(regexp = "^[a-z]{2,3}(-[A-Z]{2})?$", message = "Code must follow BCP 47 format (e.g. 'es', 'es-ES')")
    String code,
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    String name,
    @NotNull(message = "isDefault cannot be null")
    Boolean isDefault,
    @NotNull(message = "Active cannot be null")
    Boolean active
) {

}
