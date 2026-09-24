package com.icms.user_auth.rules;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.icms.user_auth.entity.UserStatusTranslation;
import com.icms.shared.entity.Language;
import com.icms.shared.exceptions.BusinessRuleException;
import com.icms.user_auth.entity.UserStatus;
import com.icms.user_auth.repository.UserStatusTranslationRepository;

@ExtendWith(MockitoExtension.class)
class UserStatusTranslationRulesTest {

    @Mock
    private UserStatusTranslationRepository userStatusTranslationRepository;

    @InjectMocks
    private UserStatusTranslationRules userStatusTranslationRules;

    private UserStatus userStatus;
    private Language language;
    private UserStatusTranslation userStatusTranslation;
    private UserStatusTranslation existingTranslation;

    @BeforeEach 
    @SuppressWarnings("null")
    void setUp() {
        userStatus = Instancio.of(UserStatus.class)
                .set(Select.field(UserStatus::getId), 1L)
                .set(Select.field(UserStatus::getCode), "ACT")
                .create();
        language = Instancio.of(Language.class)
                .set(Select.field(Language::getId), 1L)
                .set(Select.field(Language::getCode), "en-US")
                .create();
        userStatusTranslation = Instancio.of(UserStatusTranslation.class)
                .set(Select.field(UserStatusTranslation::getId), null)
                .set(Select.field(UserStatusTranslation::getCatalog), userStatus)
                .set(Select.field(UserStatusTranslation::getLanguage), language)
                .create();
        existingTranslation = Instancio.of(UserStatusTranslation.class)
                .set(Select.field(UserStatusTranslation::getId), 1L)
                .set(Select.field(UserStatusTranslation::getCatalog), userStatus)
                .set(Select.field(UserStatusTranslation::getLanguage), language)
                .create();
    }

    @Test
    // @SuppressWarnings("null")
    @DisplayName("Test for UserStatusTranslationRules canSave method")
    void testCanSave() {
        // Arrange
        // Using the setup objects: userStatus, language, userStatusTranslation
        when(userStatusTranslationRepository.findByCatalogAndLanguage(
                userStatus,
                language
            )).thenReturn(Optional.empty());

        // Act & Assert
        assertThatNoException().isThrownBy(() -> 
            userStatusTranslationRules.canSave(userStatusTranslation)
        );

        // Validation
        // Verify that no more interactions with the repository occurred after the expected call
        Mockito.verify(userStatusTranslationRepository, Mockito.times(1)).findByCatalogAndLanguage(userStatus,
                language);
        verifyNoMoreInteractions(userStatusTranslationRepository);
    }

    @Test
    @DisplayName("Test for UserStatusTranslationRules canSave method when translation already exists")
    void testCanSaveWhenTranslationExists() {
        // Arrange
        // Using the setup objects: userStatus, language, userStatusTranslation, existingTranslation
        when(userStatusTranslationRepository.findByCatalogAndLanguage(userStatus,language))
            .thenReturn(Optional.of(existingTranslation));

        // Act & Assert
        assertThatThrownBy(() -> userStatusTranslationRules.canSave(userStatusTranslation))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessage("Lan-007 context")
        .extracting("code")
        .isEqualTo("Lan-007");

        // Validation
        Mockito.verify(userStatusTranslationRepository, Mockito.times(1)).findByCatalogAndLanguage(userStatus,
                language);
        verifyNoMoreInteractions(userStatusTranslationRepository);
    }

}
