package com.icms.user_auth.service;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;

import com.icms.user_auth.dto.userstatus.UserStatusTranslationMapper;
import com.icms.shared.entity.Language;
import com.icms.shared.exceptions.BusinessRuleException;
import com.icms.user_auth.dto.userstatus.UserStatusTranslationDto;
import com.icms.user_auth.entity.UserStatus;
import com.icms.user_auth.entity.UserStatusTranslation;
import com.icms.user_auth.repository.UserStatusTranslationRepository;
import com.icms.user_auth.rules.UserStatusTranslationRules;
import com.icms.user_auth.service.daoservice.UserStatusTranslationService;

@ExtendWith(MockitoExtension.class)
@Profile("test")
public class UserStatusTranslationServiceTest {

    @Mock 
    private UserStatusTranslationRepository userStatusTranslationRepository;

    @Mock
    private UserStatusTranslationMapper userStatusTranslationMapper;

    @Mock
    private UserStatusTranslationRules userStatusTranslationRules;

    @InjectMocks 
    private UserStatusTranslationService userStatusTranslationService;

    /**
     * test to verify behavior when we create correctly a new user status translation
     */
    @Test 
    public void testCreateUserStatusTranslation() {
        // Arrange
        UserStatusTranslationDto userStatusTranslationDto = new UserStatusTranslationDto(null, 1L, 3L, "translation", null);
        UserStatus userStatus = new UserStatus("ACT", Boolean.TRUE, "Active");
        Language language = new Language("fr-FR", "French", Boolean.FALSE, Boolean.TRUE);
        UserStatusTranslation userStatusTranslation = new UserStatusTranslation( userStatus, language, "translation", null);
        
        Mockito.when(userStatusTranslationMapper.toEntity(userStatusTranslationDto)).thenReturn(userStatusTranslation);
        Mockito.when(userStatusTranslationRepository.save(userStatusTranslation)).thenReturn(userStatusTranslation);
        Mockito.when(userStatusTranslationMapper.toDto(userStatusTranslation)).thenReturn(userStatusTranslationDto);

        // Act
        UserStatusTranslationDto result = userStatusTranslationService.save(userStatusTranslationDto);

        // Assert
        Mockito.verify(userStatusTranslationRules, Mockito.times(1)).canSave(userStatusTranslation);
        assertEquals(userStatusTranslationDto, result);
    }

    /**
     * test to verify behavior when we try to create a user status translation with invalid data, like repeating an existing translation
     */
    @Test 
    public void testCreateUserStatusTranslationWithInvalidData() {
        // Arrange
        UserStatusTranslationDto userStatusTranslationDto = new UserStatusTranslationDto(null, 1L, 1L, "translation", null);
        UserStatus userStatus = new UserStatus("ACT", Boolean.TRUE, "Active");
        Language language = new Language("en-US", "English", Boolean.TRUE, Boolean.TRUE);
        UserStatusTranslation userStatusTranslation = new UserStatusTranslation( userStatus, language, "translation", null);

        Mockito.when(userStatusTranslationMapper.toEntity(userStatusTranslationDto)).thenReturn(userStatusTranslation);
        Mockito.doThrow(new BusinessRuleException("Lan-007")).when(userStatusTranslationRules).canSave(userStatusTranslation);

        // Act & Assert
        try {
            userStatusTranslationService.save(userStatusTranslationDto);
        } catch (BusinessRuleException e) {
            assertEquals("Lan-007 context", e.getMessage());
        }

        // Verify that the business rule was checked
        Mockito.verify(userStatusTranslationRules, Mockito.times(1)).canSave(userStatusTranslation);
        Mockito.verifyNoInteractions(userStatusTranslationRepository);
        Mockito.verify(userStatusTranslationMapper, Mockito.never()).toDto(userStatusTranslation);
    }

}
