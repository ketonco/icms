package com.icms.user_auth.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.icms.user_auth.dto.user.CreateUserDto;
import com.icms.user_auth.dto.user.UserMapperImpl;
import com.icms.user_auth.dto.userprofile.UserProfileDto;
//import com.icms.user_auth.dto.user.UserMapperImpl;
import com.icms.user_auth.dto.userprofile.UserProfileMapper;
import com.icms.user_auth.dto.userstatus.UserStatusMapper;
import com.icms.user_auth.dto.usertype.UserTypeMapper;
import com.icms.user_auth.entity.User;
import com.icms.user_auth.entity.UserStatus;
import com.icms.user_auth.entity.UserType;

public class UserMapperTest {

    @Mock 
    UserProfileMapper userProfileMapper;
    @Mock 
    UserTypeMapper userTypeMapper;
    @Mock 
    UserStatusMapper userStatusMapper;

    @InjectMocks 
    UserMapperImpl mapper;

    // TODO (pending P-12): replace manual Mockito initialization with MockitoExtension per project test conventions.
    @BeforeEach 
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test 
    @DisplayName ("test createuserdto mapped to entity correctly")
    void mappingCreateUserDtoToUser(){
        // Arrange
        CreateUserDto createUserDto = Instancio.create(CreateUserDto.class);

        //Act
        User user = mapper.toEntity(createUserDto);

        //Assert
        assertThat(user.getId()).isNull();
        assertThat(user.getUsername()).isEqualTo(createUserDto.username());
        assertThat(user.getStatus()).isNull();
        assertThat(user.getTypes()).isNull();   
    }

    @Test 
    @SuppressWarnings("null")
    @DisplayName ("test entity mapped to createUserdto")
    void mappingUserToCreateUserDto(){
        // Arrange
        Set<UserType> roles = new HashSet<>(List.of(new UserType("ADM", Boolean.TRUE, "ADMIN")));

        User user = Instancio.of(User.class)
            .set(Select.field(User::getStatus), new UserStatus("ACT", Boolean.TRUE, "ACTIVE"))
            .set(Select.field(User::getTypes), roles)
            .create();
        
        UserProfileDto userProfileDto = new UserProfileDto(
            user.getProfile().getId(), 
            user.getProfile().getFirstName(), 
            user.getProfile().getLastName(), 
            user.getProfile().getAvatarUrl(), 
            null, 
            null, 
            user.getEmail());

        given(userProfileMapper.toDto(any())).willReturn(userProfileDto);
        given(userTypeMapper.convertToString(any())).willReturn("ADM");
        given(userStatusMapper.convertEntityToString(any())).willReturn("ACT");

        //Act
        CreateUserDto createUserDto = mapper.toCreateUseDto(user);

        //Assert
        assertThat(createUserDto.username()).isEqualTo(user.getUsername());
        assertThat(createUserDto.profile().firstName()).isEqualTo(user.getProfile().getFirstName());
        assertThat(createUserDto.status()).isEqualTo(user.getStatus().getCode());
        assertThat(createUserDto.types()).contains("ADM");       

        
    }


}
