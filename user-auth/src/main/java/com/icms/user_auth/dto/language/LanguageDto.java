package com.icms.user_auth.dto.language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LanguageDto(
    @NotBlank(message = "Code cannot be blank")
    @NotNull(message = "Code cannot be null")
    @Size(min = 2, max = 3, message = "Code must be between 2 and 3 characters")
    String code,
    @NotBlank(message = "Name cannot be blank")
    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    String name,
    @NotNull(message = "isDefault cannot be null")
    Boolean isDefault,
    @NotNull(message = "Active cannot be null")
    Boolean active
) {

}
