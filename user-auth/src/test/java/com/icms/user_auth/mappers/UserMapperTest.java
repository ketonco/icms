package com.icms.user_auth.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @Mock 
    UserProfileMapper userProfileMapper;
    @Mock 
    UserTypeMapper userTypeMapper;
    @Mock 
    UserStatusMapper userStatusMapper;

    @InjectMocks 
    UserMapperImpl mapper;

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
        
        //Act
        CreateUserDto createUserDto = mapper.toCreateUserDto(user);

        //Assert
        assertThat(createUserDto.username()).isEqualTo(user.getUsername());
        assertThat(createUserDto.profile().firstName()).isEqualTo(user.getProfile().getFirstName());       

        
    }


}
