package com.icms.user_auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

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
            .set(Select.field(UserStatus::getCode), "PEN")
            .create();

        userType = Instancio.of(UserType.class)
            .set(Select.field(UserType::getCode), "USR")
            .create();

        userTypes = new HashSet<>(List.of(userType));

        user = Instancio.create(User.class);

        createUserDto = Instancio.create(CreateUserDto.class);
    }

    @Test 
    @DisplayName("Test creating a new user")
    void testCreateUser() {
        // Arrange
        // we use AtomicReference to capture the encoded password during the test, since the password is encoded inside the service method
        // AtomicReference is useful to capture the value inside the lambda expression
        AtomicReference<String> passwordAtSave = new AtomicReference<>();
        AtomicReference<UserStatus> statusAtSave = new AtomicReference<>();
        AtomicReference<Set<UserType>> typesAtSave = new AtomicReference<>();
        AtomicReference<String> passwordAfterSave = new AtomicReference<>();

        CreateUserDto userDtoCreated = Instancio.of(CreateUserDto.class)
            .set(Select.field(CreateUserDto::username), createUserDto.username())
            .ignore(Select.field(CreateUserDto::password))
            .create();

        Mockito.when(userMapper.toEntity(createUserDto)).thenReturn(user);
        Mockito.when(userStatusRepository.findByCode("PEN")).thenReturn(Optional.of(userStatus));
        Mockito.when(userTypeRepository.findByCode("USR")).thenReturn(Optional.of(userType));
        Mockito.when(passwordEncoder.encode(createUserDto.password())).thenReturn("passwordEncoded");

        Mockito.doNothing().when(userRules).canCreate(user);

        Mockito.when(userRepository.save(user)).thenAnswer(invocation -> {
            User toSave = invocation.getArgument(0);
            passwordAtSave.set(toSave.getPassword());
            statusAtSave.set(toSave.getStatus());
            typesAtSave.set(toSave.getTypes());
            return toSave;
        });

        Mockito.when(userMapper.toCreateUserDto(user)).thenAnswer(invocation -> {
            User userArg = invocation.getArgument(0);
            passwordAfterSave.set(userArg.getPassword());
            return userDtoCreated;
        });

        // Act
        // Call the method to create a user here, e.g., userService.createUser(createUserDto);
        CreateUserDto result = userService.createUserDto(createUserDto);

        // Assert
        // Add assertions to verify the user was created correctly
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo(userDtoCreated.username());
        assertThat(passwordAfterSave.get()).isNull();
        assertThat(passwordAtSave.get()).isEqualTo("passwordEncoded");
        assertThat(statusAtSave.get()).isEqualTo(userStatus);
        assertThat(typesAtSave.get()).isEqualTo(userTypes);

        Mockito.verify(userRules, Mockito.times(1)).canCreate(user);
    }

}
