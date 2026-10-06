package com.icms.user_auth.dto.usertype;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.icms.shared.dto.IdentifiableDtoImpl;
public record UserTypeTranslationDto(
    Long id,
    @NotNull(message = "{usrtypetrans.catalogid.null}")
    Long catalogId,
    @NotNull(message = "{usrtypetrans.languageid.null}")
    Long languageId,
    @NotBlank(message = "{usrtypetrans.translation.blank}")
    String translation,
    String description
) implements IdentifiableDtoImpl<Long> {

}
