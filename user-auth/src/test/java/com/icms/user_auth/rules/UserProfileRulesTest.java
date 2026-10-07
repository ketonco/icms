package com.icms.user_auth.rules;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.icms.user_auth.repository.UserProfileRepository;
import com.icms.user_auth.repository.UserRepository;
import com.icms.user_auth.entity.User;
import com.icms.user_auth.rules.dao.UserProfileRules;
import com.icms.shared.entity.UUIDAuditableEntity;
import com.icms.shared.exceptions.BusinessRuleException;
import com.icms.shared.exceptions.EntityNotFoundException;
import com.icms.user_auth.entity.UserProfile;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.UUID;

import org.instancio.Instancio;
import org.instancio.Select;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
public class UserProfileRulesTest {

    @Mock
    UserProfileRepository userProfileRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks 
    UserProfileRules userProfileRules;

    @Test 
    @DisplayName ("Test can save user profile successfully")
    public void testCanSaveUserProfileSuccessfully() {
        // Arrange
        User user = Instancio.create(User.class);
        UserProfile userProfile = Instancio.of(UserProfile.class)
            .set(Select.field(UUIDAuditableEntity::getId), null)
            .set(Select.field(UserProfile::getUser), user)
            .create();

        Mockito.when(userRepository.existsById(user.getId())).thenReturn(true);
        // Act && Assert
        assertThatNoException().isThrownBy(() -> 
            userProfileRules.canSave(userProfile)
        );

        // Validation
        Mockito.verify(userRepository, Mockito.times(1)).existsById(user.getId());
    }

    @Test 
    @DisplayName ("Test cannot save user profile when user does not exist")
    public void testCannotSaveUserProfileWhenUserDoesNotExist() {
        // Arrange
        User user = Instancio.create(User.class);
        UserProfile userProfile = Instancio.of(UserProfile.class)
            .set(Select.field(UUIDAuditableEntity::getId), null)
            .set(Select.field(UserProfile::getUser), user)
            .create();

        Mockito.when(userRepository.existsById(user.getId())).thenReturn(false);

        // Act && Assert

        assertThatThrownBy(() -> userProfileRules.canSave(userProfile))
        .isInstanceOf(BusinessRuleException.class)
        .extracting("code")
        .isEqualTo("UsrProf-002");

        // Validation
        Mockito.verify(userRepository, Mockito.times(1)).existsById(user.getId());
    }

    @Test 
    @DisplayName ("Test can update user profile successfully")
    public void testCanUpdateUserProfileSuccessfully() {
        // Arrange
        User user = Instancio.create(User.class);

        UserProfile userProfile = Instancio.of(UserProfile.class)
            .set(Select.field(UUIDAuditableEntity::getId), new UUID(0L, 1L))
            .set(Select.field(UserProfile::getUser), user)
            .create();

        Mockito.when(userRepository.existsById(user.getId())).thenReturn(true);
        Mockito.when(userProfileRepository.findById(userProfile.getId())).thenReturn(Optional.of(userProfile));
        Mockito.when(userProfileRepository.existsById(userProfile.getId())).thenReturn(true);

        // Act && Assert
        assertThatNoException().isThrownBy(() -> 
            userProfileRules.canUpdate(userProfile)
        );

        // Validation
        Mockito.verify(userRepository, Mockito.times(1)).existsById(user.getId());
        Mockito.verify(userProfileRepository, Mockito.times(1)).findById(userProfile.getId());
        Mockito.verify(userProfileRepository, Mockito.times(1)).existsById(userProfile.getId());
    }

    @Test 
    @DisplayName("Test cannot update user profile when it comes with userProfile ID null")
    public void testCannotUpdateUserProfileWhenIdIsNull() {
        // Arrange
        User user = Instancio.create(User.class);
        UserProfile userProfile = Instancio.of(UserProfile.class)
            .set(Select.field(UUIDAuditableEntity::getId), null)
            .set(Select.field(UserProfile::getUser), user)
            .create();

        // Act && Assert
        assertThatThrownBy(() -> userProfileRules.canUpdate(userProfile))
        .isInstanceOf(BusinessRuleException.class)
        .extracting("code")
        .isEqualTo("Ent-002");
    }

    @Test 
    @DisplayName("Test cannot update user profile when it does not exist")
    public void testCannotUpdateUserProfileWhenItDoesNotExist() {
        // Arrange
        User user = Instancio.create(User.class);
        UserProfile userProfile = Instancio.of(UserProfile.class)
            .set(Select.field(UUIDAuditableEntity::getId), new UUID(0L, 1L))
            .set(Select.field(UserProfile::getUser), user)
            .create();
            
        Mockito.when(userProfileRepository.existsById(userProfile.getId())).thenReturn(false);

        // Act && Assert
        assertThatThrownBy(() -> userProfileRules.canUpdate(userProfile))
        .isInstanceOf(EntityNotFoundException.class)
        .extracting("code")
        .isEqualTo("Ent-001");

        // Validation
        Mockito.verify(userProfileRepository, Mockito.times(1)).existsById(userProfile.getId());
        Mockito.verify(userProfileRepository, Mockito.never()).findById(userProfile.getId());
        Mockito.verify(userRepository, Mockito.never()).existsById(user.getId());        
    }

}
