package com.icms.user_auth.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.instancio.Instancio;
import org.instancio.Select;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.icms.shared.entity.BaseCatalogEntity;
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
    @SuppressWarnings("null")
    @DisplayName("Test if the LanguageMapper returns dto correctly when mapping from entity")
    void mapFromEntityToDto() {
        // Given
        Language language = Instancio.of(Language.class)
                .set(Select.field(BaseCatalogEntity::getName), "Spanish")
                .create();
    
        // When
        LanguageDto languageDto = languageMapper.toDto(language);

        // Then
        assertThat(languageDto).isNotNull();
        assertThat(languageDto.name()).isEqualTo("Spanish"); // solo para corroborar el valor esperado
        assertThat(languageDto.code()).isEqualTo(language.getCode());
        assertThat(languageDto.isDefault()).isEqualTo(language.getIsDefault());
        assertThat(languageDto.active()).isEqualTo(language.getActive());
    }

    @Test
    @DisplayName("Test if the LanguageMapper returns entity correctly when mapping from dto")
    void createEntityFromDto() {
        // Given
        LanguageDto languageDto = Instancio.create(LanguageDto.class);

        // When
        Language language = languageMapper.toEntity(languageDto);

        // Then
        assertThat(language).isNotNull();
        assertThat(language.getName()).isEqualTo(languageDto.name());
        assertThat(language.getCode()).isEqualTo(languageDto.code());
        assertThat(language.getIsDefault()).isEqualTo(languageDto.isDefault());
        assertThat(language.getActive()).isEqualTo(languageDto.active());
    }

    @Test
    @DisplayName("Test if the LanguageMapper updates entity correctly when mapping from dto")
    void updateEntityFromDto() {
        // Given
        // Existing language entity extracted from the database with an ID that should not be updated
        Language existingLanguage = Instancio.create(Language.class);
        Long existingLanguageId = existingLanguage.getId();
        // Language DTO with new values to update the existing entity
        LanguageDto languageDto = Instancio.create(LanguageDto.class);

        // When
        languageMapper.updateEntityFromDto(languageDto, existingLanguage);

        // Then
        assertThat(existingLanguage.getName()).isEqualTo(languageDto.name());
        assertThat(existingLanguage.getCode()).isEqualTo(languageDto.code());
        assertThat(existingLanguage.getIsDefault()).isEqualTo(languageDto.isDefault());
        assertThat(existingLanguage.getActive()).isEqualTo(languageDto.active());
        assertThat(existingLanguage.getId()).isEqualTo(existingLanguageId); // id should not be updated
    }

}
