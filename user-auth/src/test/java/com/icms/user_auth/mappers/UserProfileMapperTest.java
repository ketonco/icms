package com.icms.user_auth.mappers;

import com.icms.user_auth.dto.userprofile.UserProfileDto;
import com.icms.user_auth.dto.userprofile.UserProfileMapper;
import com.icms.user_auth.entity.User;
import com.icms.user_auth.entity.UserProfile;

import org.junit.jupiter.api.DisplayName;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserProfileMapperTest {

    private UserProfileMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UserProfileMapper.class);
    }

    @Test 
    @DisplayName("Test mapping from UserProfile entity to UserProfile DTO")
    void mapFromEntityToDto() {
        // Arrange
        UserProfile entity = Instancio.create(UserProfile.class);

        // Act
        UserProfileDto dto = mapper.toDto(entity);

        // Assert
        // Verify that the mapping is correct
        assertThat(dto.id()).isEqualTo(entity.getId());
        assertThat(dto.firstName()).isEqualTo(entity.getFirstName());
        assertThat(dto.lastName()).isEqualTo(entity.getLastName());
        assertThat(dto.avatarUrl()).isEqualTo(entity.getAvatarUrl());
        // verify that the email mapping is correct
        assertThat(dto.email()).isEqualTo(entity.getUser().getEmail());
    }

    @Test 
    @DisplayName("Test mapping from UserProfile DTO to UserProfile entity")
    void createEntityFromDto() {
        // Arrange
        UserProfileDto dto = Instancio.create(UserProfileDto.class);

        // Act
        UserProfile entity = mapper.toEntity(dto);

        // Assert
        // Verify that the mapping is correct
        assertThat(entity.getFirstName()).isEqualTo(dto.firstName());
        assertThat(entity.getLastName()).isEqualTo(dto.lastName());
        assertThat(entity.getAvatarUrl()).isEqualTo(dto.avatarUrl());
        // validate that the ID is null since it should not be set by the mapper 
        assertThat(entity.getId()).isNull();
        // validate that user have to be null
        assertThat(entity.getUser()).isNull();
    }

    
    @Test 
    @DisplayName("Test updating UserProfile entity from UserProfile DTO")
    void updateEntityFromDto() {
        // Arrange
        UserProfile existingEntity = Instancio.create(UserProfile.class);
        User existingUser = existingEntity.getUser();
        UUID existingUserProfileId = existingEntity.getId();
        UserProfileDto dto = Instancio.create(UserProfileDto.class);

        // Act
        mapper.updateEntityFromDto(dto, existingEntity);

        // Assert
        assertThat(existingEntity.getFirstName()).isEqualTo(dto.firstName());
        assertThat(existingEntity.getLastName()).isEqualTo(dto.lastName());
        assertThat(existingEntity.getAvatarUrl()).isEqualTo(dto.avatarUrl());
        // validate that user remains unchanged
        assertThat(existingEntity.getUser()).isNotNull();
        assertThat(existingEntity.getUser()).isSameAs(existingUser);
        // validate that the ID remains unchanged
        assertThat(existingEntity.getId()).isEqualTo(existingUserProfileId);
    }

}
