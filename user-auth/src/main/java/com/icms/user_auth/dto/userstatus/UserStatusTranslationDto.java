package com.icms.user_auth.dto.userstatus;

import jakarta.validation.constraints.NotBlank;

public record UserStatusTranslationDto(
    Long id,
    @NotBlank(message = "Catalog ID cannot be blank")
    Long catalogId,
    @NotBlank(message = "Language ID cannot be blank")
    Long languageId,
    @NotBlank(message = "Translation cannot be blank")
    String translation,
    String description
) {

}
