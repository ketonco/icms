package com.icms.user_auth.mappers;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.icms.shared.entity.Language;
import com.icms.user_auth.dto.language.LanguageDto;
import com.icms.user_auth.dto.language.LanguageMapper;
public class LanguageMapperTest {

    private LanguageMapper languageMapper;

    @BeforeEach
    void setUp() {
        languageMapper = Mappers.getMapper(LanguageMapper.class);
    }

    @Test 
    @DisplayName("Test if the LanguageMapper returns dto correctly when mapping from entity")
    void mapFromEntityToDto() {
        // Given
        Language language = new Language();
        language.setId(1L);
        language.setName("Spanish");
        language.setCode("es");
        language.setIsDefault(true);
        language.setActive(true);
    

        // When
        LanguageDto languageDto = languageMapper.toDto(language);

        // Then
        assertNotNull(languageDto);
        assertEquals("Spanish", languageDto.name());
        assertEquals("es", languageDto.code());
        assertEquals(true, languageDto.isDefault());
        assertEquals(true, languageDto.active());
    }

    @Test
    @DisplayName("Test if the LanguageMapper returns entity correctly when mapping from dto")
    void mapFromDtoToEntity() {
        // Given
        LanguageDto languageDto = new LanguageDto(Long.valueOf(1L), "es", "Spanish", true, true);

        // When
        Language language = languageMapper.toEntity(languageDto);

        // Then
        assertNotNull(language);
        assertEquals("Spanish", language.getName());
        assertEquals("es", language.getCode());
        assertEquals(true, language.getIsDefault());
        assertEquals(true, language.getActive());
    }

}
