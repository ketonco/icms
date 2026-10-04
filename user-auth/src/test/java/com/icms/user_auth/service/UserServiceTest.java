package com.icms.user_auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.icms.user_auth.dto.user.CreateUserDto;
import com.icms.user_auth.dto.user.UserMapper;
import com.icms.user_auth.entity.User;
import com.icms.user_auth.entity.UserStatus;
import com.icms.user_auth.entity.UserType;
import com.icms.user_auth.repository.UserRepository;
import com.icms.user_auth.repository.UserStatusRepository;
import com.icms.user_auth.repository.UserTypeRepository;
import com.icms.user_auth.rules.dao.UserRules;
import com.icms.user_auth.service.daoservice.UserService;

@SuppressWarnings ("null")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock 
    UserRepository userRepository;

    @Mock
    UserStatusRepository userStatusRepository;

    @Mock
    UserTypeRepository userTypeRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    UserRules userRules;

    @Mock 
    PasswordEncoder passwordEncoder;

    @InjectMocks 
    UserService userService;

    UserStatus userStatus;
    UserType userType;
    Set<UserType> userTypes;
    User user;
    CreateUserDto createUserDto;

    @BeforeEach 
    void setUp() {
        userStatus = Instancio.of(UserStatus.class)
            .set(Select.field(UserStatus::getCode), "INA")
            .create();

        userType = Instancio.of(UserType.class)
            .set(Select.field(UserType::getCode), "USR")
            .create();

        userTypes = new HashSet<>(List.of(userType));

        user = Instancio.of(User.class)
            .set(Select.field(User::getStatus), userStatus)
            .set(Select.field(User::getTypes), userTypes)
            .create();

        createUserDto = Instancio.create(CreateUserDto.class);
    }

    @Test 
    @DisplayName("Test creating a new user")
    void testCreateUser() {
        // Arrange
        CreateUserDto userDtoCreated = Instancio.of(CreateUserDto.class)
            .set(Select.field(CreateUserDto::username), createUserDto.username())
            .ignore(Select.field(CreateUserDto::password))
            .create();

        Mockito.when(userMapper.toEntity(createUserDto)).thenReturn(user);
        Mockito.when(userStatusRepository.findByCode("INA")).thenReturn(Optional.of(userStatus));
        Mockito.when(userTypeRepository.findByCode("USR")).thenReturn(Optional.of(userType));
        Mockito.when(passwordEncoder.encode(createUserDto.password())).thenReturn("encodedPassword");
        Mockito.doNothing().when(userRules).canCreate(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userMapper.toCreateUserDto(user)).thenReturn(userDtoCreated);

        // Act
        // Call the method to create a user here, e.g., userService.createUser(createUserDto);
        CreateUserDto result = userService.createUserDto(createUserDto);

        // Assert
        // Add assertions to verify the user was created correctly
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo(userDtoCreated.username());
    }

}
