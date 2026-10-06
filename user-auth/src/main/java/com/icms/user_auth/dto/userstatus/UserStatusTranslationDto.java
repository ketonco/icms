package com.icms.user_auth.dto.userstatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.icms.shared.dto.IdentifiableDtoImpl;

public record UserStatusTranslationDto(
    Long id,
    @NotNull(message = "{usrstatustrans.catalogid.null}")
    Long catalogId,
    @NotNull(message = "{usrstatustrans.languageid.null}")
    Long languageId,
    @NotBlank(message = "{usrstatustrans.translation.blank}")
    String translation,
    String description
) implements IdentifiableDtoImpl<Long> {

}
