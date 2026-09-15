package com.icms.user_auth.dto.userstatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserStatusTranslationDto(
    Long id,
    @NotNull(message = "Catalog ID cannot be blank")
    Long catalogId,
    @NotNull(message = "Language ID cannot be blank")
    Long languageId,
    @NotBlank(message = "Translation cannot be blank")
    String translation,
    String description
) {

}
