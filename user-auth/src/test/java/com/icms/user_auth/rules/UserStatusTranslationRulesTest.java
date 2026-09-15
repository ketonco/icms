package com.icms.user_auth.rules;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;

import com.icms.user_auth.entity.UserStatusTranslation;
import com.icms.shared.entity.Language;
import com.icms.shared.exceptions.BusinessRuleException;
import com.icms.user_auth.entity.UserStatus;
import com.icms.user_auth.repository.UserStatusTranslationRepository;

@ExtendWith(MockitoExtension.class)
@Profile("test")
public class UserStatusTranslationRulesTest {

    @Mock 
    private UserStatusTranslationRepository userStatusTranslationRepository;

    @InjectMocks 
    private UserStatusTranslationRules userStatusTranslationRules;

    @Test 
    @DisplayName("Test for UserStatusTranslationRules canSave method")
    public void testCanSave() {
        // Arrange
        UserStatus userStatus = new UserStatus("ACT", Boolean.TRUE, "ACTIVE");
        userStatus.setId(1L);
        Language language = new Language("en-US", "English", Boolean.TRUE, Boolean.TRUE);
        UserStatusTranslation userStatusTranslation = new UserStatusTranslation(userStatus, language, "Active", null);

        when(userStatusTranslationRepository.findByCatalogAndLanguage(userStatusTranslation.getCatalog(), userStatusTranslation.getLanguage())).thenReturn(Optional.empty());

        // Act & Assert
        assertDoesNotThrow(() -> {
            userStatusTranslationRules.canSave(userStatusTranslation);
        });

        // Validation
        Mockito.verify(userStatusTranslationRepository, Mockito.times(1)).findByCatalogAndLanguage(userStatus, language);
    }

    @Test 
    @DisplayName("Test for UserStatusTranslationRules canSave method when translation already exists")
    public void testCanSaveWhenTranslationExists() {
        // Arrange
        UserStatus userStatus = new UserStatus("ACT", Boolean.TRUE, "ACTIVE");
        userStatus.setId(1L);
        Language language = new Language("en-US", "English", Boolean.TRUE, Boolean.TRUE);
        UserStatusTranslation userStatusTranslation = new UserStatusTranslation(userStatus, language, "Active", null);
        UserStatusTranslation existingTranslation = new UserStatusTranslation(userStatus, language, "Active", null);
        existingTranslation.setId(1L);

        when(userStatusTranslationRepository.findByCatalogAndLanguage(userStatusTranslation.getCatalog(), userStatusTranslation.getLanguage())).thenReturn(Optional.of(existingTranslation));

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            userStatusTranslationRules.canSave(userStatusTranslation);
        });

        assertEquals("Lan-007 context", exception.getMessage()); 

        // Validation
        Mockito.verify(userStatusTranslationRepository, Mockito.times(1)).findByCatalogAndLanguage(userStatus, language);
    }

}
