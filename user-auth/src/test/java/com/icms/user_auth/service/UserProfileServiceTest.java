package com.icms.user_auth.service;

import org.instancio.Instancio;
import org.instancio.Select;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import com.icms.user_auth.entity.User;
import com.icms.user_auth.entity.UserProfile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.icms.shared.exceptions.EntityNotFoundException;
import com.icms.user_auth.dto.userprofile.UserProfileDto;
import com.icms.user_auth.dto.userprofile.UserProfileMapper;
import com.icms.user_auth.repository.UserProfileRepository;
import com.icms.user_auth.repository.UserRepository;
import com.icms.user_auth.rules.dao.UserProfileRules;
import com.icms.user_auth.service.daoservice.UserProfileService;

@SuppressWarnings ("null")
@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {


    @Mock 
    private UserProfileRepository repository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserProfileMapper mapper;
    @Mock
    private UserProfileRules rules;
    @InjectMocks 
    private UserProfileService service;

    UserProfileDto dto;
    UserProfileDto expectedDto;

    @BeforeEach 
    void setUp() {
        dto = Instancio.create(UserProfileDto.class);

        expectedDto = Instancio.of(UserProfileDto.class)
            .set(Select.field(UserProfileDto::email), dto.email())
            .set(Select.field(UserProfileDto::firstName), dto.firstName())
            .set(Select.field(UserProfileDto::lastName), dto.lastName())
            .create();
    }

    @Test     
    @DisplayName ("test save user profile")
    void testSaveUserProfile() {
        // Arrange
        // memory user that matches the dto's email
        User user = Instancio.of(User.class)
            .set(Select.field(User::getEmail), dto.email()) 
            .create();

        // memory user profile that matches the dto's first and last name
        UserProfile userProfile = Instancio.of(UserProfile.class)
            .set(Select.field(UserProfile::getFirstName), dto.firstName())
            .set(Select.field(UserProfile::getLastName), dto.lastName())
            .create();

        Mockito.when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        Mockito.when(repository.save(userProfile)).thenReturn(userProfile);
        Mockito.when(repository.findByUser(user)).thenReturn(Optional.of(userProfile));
        Mockito.when(mapper.toDto(userProfile)).thenReturn(expectedDto);

        // mock for mapper.updateEntityFromDto
        Mockito.doNothing().when(mapper).updateEntityFromDto(dto, userProfile);
        Mockito.doNothing().when(rules).canSave(userProfile);

        // Act
        UserProfileDto result = service.save(dto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(dto.email());
        assertThat(result.firstName()).isEqualTo(userProfile.getFirstName());
        assertThat(result.lastName()).isEqualTo(userProfile.getLastName());

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(dto.email());
        Mockito.verify(rules, Mockito.times(1)).canSave(userProfile);
        Mockito.verify(mapper, Mockito.times(1)).updateEntityFromDto(dto, userProfile);
        Mockito.verify(repository, Mockito.times(1)).save(userProfile);
    }

    @Test     
    @DisplayName ("test save user profile when user does not exist by email")
    void testSaveUserProfileWhenUserDoesNotExistByEmail() {
        // Arrange
        Mockito.when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.save(dto))
            .isInstanceOf(EntityNotFoundException.class)
            .extracting("code")
            .isEqualTo("Usr-001");

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(dto.email());
        Mockito.verify(repository, Mockito.never()).findByUser(Mockito.any());
        Mockito.verify(rules, Mockito.never()).canSave(Mockito.any());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test     
    @DisplayName ("test save user profile when user profile does not exist by user")
    void testSaveUserProfileWhenUserProfileDoesNotExistByUser() {
        // Arrange
        User user = Instancio.of(User.class)
            .set(Select.field(User::getEmail), dto.email()) 
            .create();

        Mockito.when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        Mockito.when(repository.findByUser(user)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.save(dto))
            .isInstanceOf(EntityNotFoundException.class)
            .extracting("code")
            .isEqualTo("UsrProf-001");

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(dto.email());
        Mockito.verify(repository, Mockito.times(1)).findByUser(user);
        Mockito.verify(rules, Mockito.never()).canSave(Mockito.any());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test     
    @DisplayName ("test update user profile when user profile exists by user")
    void testUpdateUserProfileWhenUserProfileExistsByUser() {
        // Arrange
        // persistent user that matches the dto's email

        User user = Instancio.of(User.class)
            .set(Select.field(User::getEmail), dto.email()) 
            .create();

        // persistent user profile that matches the dto's first and last name
        UserProfile userProfile = Instancio.of(UserProfile.class)
            .set(Select.field(UserProfile::getFirstName), dto.firstName())
            .set(Select.field(UserProfile::getLastName), dto.lastName())
            .create();

        Mockito.when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        Mockito.when(repository.save(userProfile)).thenReturn(userProfile);
        Mockito.when(repository.findByUser(user)).thenReturn(Optional.of(userProfile));
        Mockito.when(mapper.toDto(userProfile)).thenReturn(expectedDto);

        // mock for mapper.updateEntityFromDto
        Mockito.doNothing().when(mapper).updateEntityFromDto(dto, userProfile);
        Mockito.doNothing().when(rules).canUpdate(userProfile);

        // Act
        UserProfileDto result = service.update(dto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(dto.email());
        assertThat(result.firstName()).isEqualTo(userProfile.getFirstName());
        assertThat(result.lastName()).isEqualTo(userProfile.getLastName());

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(dto.email());
        Mockito.verify(rules, Mockito.times(1)).canUpdate(userProfile);
        Mockito.verify(mapper, Mockito.times(1)).updateEntityFromDto(dto, userProfile);
        Mockito.verify(repository, Mockito.times(1)).save(userProfile);
    }



}
