package com.icms.user_auth.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.icms.shared.entity.Language;
import com.icms.shared.exceptions.BusinessRuleException;
import com.icms.user_auth.repository.LanguageRepository;
import com.icms.user_auth.rules.dao.LanguageRules;

import java.util.Optional;

import org.instancio.Instancio;
import org.instancio.Select;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests of {@link LanguageRules}, the catalog rules that guard the
 * language entity. Covers the three renumbered business codes of this class:
 * duplicated name ({@code Lan-001}), default language on delete
 * ({@code Ent-004}) and default language on update ({@code Ent-005}).
 */
@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
public class LanguageRulesTest {

    @Mock
    LanguageRepository languageRepository;

    @InjectMocks
    LanguageRules languageRules;

    Language newLanguage;
    Language defaultLanguage;
    Language otherLanguage;

    @BeforeEach
    public void setUp() {
        newLanguage = Instancio.of(Language.class)
            .set(Select.field(Language::getId), null)
            .set(Select.field(Language::getCode), "pt-BR")
            .set(Select.field(Language::getName), "Portuguese")
            .set(Select.field(Language::getIsDefault), false)
            .create();

        defaultLanguage = Instancio.of(Language.class)
            .set(Select.field(Language::getId), 1L)
            .set(Select.field(Language::getCode), "pt-BR")
            .set(Select.field(Language::getName), "Portuguese")
            .set(Select.field(Language::getIsDefault), true)
            .create();

        otherLanguage = Instancio.of(Language.class)
            .set(Select.field(Language::getId), 2L)
            .set(Select.field(Language::getCode), "fr-FR")
            .set(Select.field(Language::getName), "French")
            .set(Select.field(Language::getIsDefault), false)
            .create();
    }

    @Test
    @DisplayName("Test canSave rejects a duplicated language name with code Lan-001")
    public void canSaveRejectsDuplicatedNameWithNewCode() {
        // Arrange
        Mockito.when(languageRepository.findByCode("pt-BR")).thenReturn(Optional.empty());
        Mockito.when(languageRepository.findByName("Portuguese")).thenReturn(Optional.of(otherLanguage));

        // Act & Assert
        assertThatThrownBy(() -> languageRules.canSave(newLanguage))
            .isInstanceOf(BusinessRuleException.class)
            .extracting("code")
            .isEqualTo("Lan-001");

        Mockito.verify(languageRepository, Mockito.times(1)).findByName("Portuguese");
    }

    @Test
    @DisplayName("Test canSave allows a unique language name")
    public void canSaveAllowsUniqueName() {
        // Arrange
        Mockito.when(languageRepository.findByCode("pt-BR")).thenReturn(Optional.empty());
        Mockito.when(languageRepository.findByName("Portuguese")).thenReturn(Optional.empty());

        // Act && Assert
        assertThatNoException().isThrownBy(() -> languageRules.canSave(newLanguage));

        // Validation
        Mockito.verify(languageRepository, Mockito.times(1)).findByCode("pt-BR");
        Mockito.verify(languageRepository, Mockito.times(1)).findByName("Portuguese");
    }

    @Test
    @DisplayName("Test canDelete rejects the default language with code Ent-004")
    public void canDeleteRejectsDefaultLanguageWithNewCode() {
        // Arrange
        Mockito.when(languageRepository.existsById(1L)).thenReturn(true);
        Mockito.when(languageRepository.findByIsDefault(true)).thenReturn(Optional.of(otherLanguage));

        // Act & Assert
        assertThatThrownBy(() -> languageRules.canDelete(defaultLanguage))
            .isInstanceOf(BusinessRuleException.class)
            .extracting("code")
            .isEqualTo("Ent-004");

        Mockito.verify(languageRepository, Mockito.times(1)).findByIsDefault(true);
    }

    @Test
    @DisplayName("Test canUpdate rejects the default language with code Ent-005")
    public void canUpdateRejectsDefaultLanguageWithNewCode() {
        // Arrange
        Mockito.when(languageRepository.existsById(1L)).thenReturn(true);
        Mockito.when(languageRepository.findByCode("pt-BR")).thenReturn(Optional.empty());
        Mockito.when(languageRepository.findByName("Portuguese")).thenReturn(Optional.empty());
        Mockito.when(languageRepository.findByIsDefault(true)).thenReturn(Optional.of(otherLanguage));

        // Act & Assert
        assertThatThrownBy(() -> languageRules.canUpdate(defaultLanguage))
            .isInstanceOf(BusinessRuleException.class)
            .extracting("code")
            .isEqualTo("Ent-005");

        Mockito.verify(languageRepository, Mockito.times(1)).findByIsDefault(true);
    }

    @Test
    @DisplayName("Test canDelete allows a language that is not the default one")
    public void canDeleteAllowsNonDefaultLanguage() {
        // Arrange
        Language nonDefaultLanguage = Instancio.of(Language.class)
            .set(Select.field(Language::getId), 3L)
            .set(Select.field(Language::getCode), "es-ES")
            .set(Select.field(Language::getName), "Spanish")
            .set(Select.field(Language::getIsDefault), false)
            .create();
        Mockito.when(languageRepository.existsById(3L)).thenReturn(true);

        // Act && Assert
        assertThatNoException().isThrownBy(() -> languageRules.canDelete(nonDefaultLanguage));

        // Validation
        Mockito.verify(languageRepository, Mockito.times(1)).existsById(3L);
    }
}
