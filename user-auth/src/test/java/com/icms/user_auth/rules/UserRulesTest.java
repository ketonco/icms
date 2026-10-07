package com.icms.user_auth.rules;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.icms.shared.exceptions.BusinessRuleException;
import com.icms.user_auth.entity.User;
import com.icms.user_auth.repository.UserRepository;
import com.icms.user_auth.rules.dao.UserRules;

@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
public class UserRulesTest {

    @Mock 
    UserRepository userRepository;

    @InjectMocks 
    UserRules userRules;

    User user;

    @BeforeEach
    public void setUp() {
        user = Instancio.of(User.class)
            .set(Select.field(User::getId), new UUID(1L, 0L)) 
            .set(Select.field(User::getEmail), "test@example.com")
            .create();
    }
    
    @Test
    @DisplayName("Test user rules canCreate method") 
    public void testCanCreate() {
        // Arrange
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        // Act
        assertThatNoException().isThrownBy(() -> 
            userRules.canCreate(user)
        );

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(user.getEmail());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(user.getUsername());
    }

    @Test
    @DisplayName("Test user rules canCreate method when email already exists") 
    public void testCanCreateWhenEmailExists() {
        // Arrange
        User existingUser = Instancio.of(User.class)
            .set(Select.field(User::getId), new UUID(0L, 0L)) 
            .set(Select.field(User::getEmail), "test@example.com")
            .create();

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThatThrownBy(() -> userRules.canCreate(user))
            .isInstanceOf(BusinessRuleException.class)
        .extracting("code")
        .isEqualTo("Usr-002");

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(user.getEmail());
        Mockito.verify(userRepository, Mockito.never()).findByUsername(user.getUsername());
    }

    @Test
    @DisplayName("Test user rules canCreate method when username already exists") 
    public void testCanCreateWhenUsernameExists() {
        // Arrange
        User existingUser = Instancio.of(User.class)
            .set(Select.field(User::getId), new UUID(0L, 0L)) 
            .set(Select.field(User::getUsername), "testuser")
            .create();

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThatThrownBy(() -> userRules.canCreate(user))
            .isInstanceOf(BusinessRuleException.class)
            .extracting("code")
            .isEqualTo("Usr-003");

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(user.getEmail());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(user.getUsername());
    }
}
