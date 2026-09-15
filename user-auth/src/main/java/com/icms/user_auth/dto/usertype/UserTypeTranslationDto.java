package com.icms.user_auth.dto.usertype;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserTypeTranslationDto(
    Long id,
    @NotNull (message = "Catalog ID cannot be blank")
    Long catalogId,
    @NotNull(message = "Language ID cannot be blank")
    Long languageId,
    @NotBlank(message = "Translation cannot be blank")
    String translation,
    String description
) {

}
