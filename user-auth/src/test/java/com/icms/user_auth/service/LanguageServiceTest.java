package com.icms.user_auth.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Profile;

import com.icms.user_auth.dto.language.LanguageMapper;
import com.icms.user_auth.dto.language.LanguageDto;
import com.icms.shared.entity.Language;
import com.icms.user_auth.repository.LanguageRepository;
import com.icms.user_auth.service.daoservice.LanguageService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@Profile("test")
public class LanguageServiceTest {

    @Mock 
    private LanguageRepository languageRepository;

    @Mock 
    private LanguageMapper languageMapper;

    @InjectMocks
    private LanguageService languageService;

    @Test 
    @DisplayName("Test for LanguageService, must return a LanguageDTO when a valid language is requested by code")
    public void testGetLanguageByCode() {
        // Arrange
        Language language = new Language();
        language.setCode("en-US");
        language.setName("English");
        language.setActive(true);
        language.setIsDefault(true);

        LanguageDto languageDto = new LanguageDto(Long.valueOf(1L), "en-US", "English", true, true);

        given(languageRepository.findByCode("en-US")).willReturn(Optional.of(language));
        given(languageMapper.toDto(language)).willReturn(languageDto);

        // Act
        LanguageDto result = languageService.findDtoByCode("en-US");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.code()).isEqualTo("en-US");
        assertThat(result.name()).isEqualTo("English");
        assertThat(result.active()).isTrue();
        assertThat(result.isDefault()).isTrue();
    }

}
