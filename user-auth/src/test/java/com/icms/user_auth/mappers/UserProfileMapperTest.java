package com.icms.user_auth.mappers;

import com.icms.user_auth.dto.userprofile.UserProfileDto;
import com.icms.user_auth.dto.userprofile.UserProfileMapper;
import com.icms.user_auth.entity.User;
import com.icms.user_auth.entity.UserProfile;
import com.icms.user_auth.repository.UserProfileRepository;

import org.junit.jupiter.api.DisplayName;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

@Profile("test")
public class UserProfileMapperTest {

    @Mock 
    UserProfileRepository userProfileRepository;

    private UserProfileMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(UserProfileMapper.class);
    }

    @Test 
    @DisplayName("Test mapping from UserProfile entity to UserProfile DTO")
    public void testEntityToDtoMapping() {
        // Arrange
        UserProfile entity = UserProfile.builder()
                .firstName("John")
                .lastName("Doe")
                .avatarUrl("http://example.com/avatar.jpg")
                .user(
                    User.builder()
                        .email("john.doe@example.com")
                        .build()
                )
                .build();
        entity.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        // Act
        UserProfileDto dto = mapper.toDto(entity);

        // Assert
        // Verify that the mapping is correct
        assert dto.id().equals(entity.getId());
        assert dto.firstName().equals(entity.getFirstName());
        assert dto.lastName().equals(entity.getLastName());
        assert dto.avatarUrl().equals(entity.getAvatarUrl());
        assert dto.email().equals(entity.getUser().getEmail());
    }

    @Test 
    @DisplayName("Test mapping from UserProfile DTO to UserProfile entity")
    public void testDtoToEntityMapping() {
        // Arrange
        Map<String, Object> contact = new HashMap<>();
        Map<String, Object> prefs = new HashMap<>();
        
        UserProfileDto dto = new UserProfileDto(
                null,
                "John",
                "Doe",
                "http://example.com/avatar.jpg",
                contact,
                prefs,
                "john.doe@example.com"
        );

        // Act
        UserProfile entity = mapper.toEntity(dto);

        // Assert
        // Verify that the mapping is correct
        assert entity.getFirstName().equals(dto.firstName());
        assert entity.getLastName().equals(dto.lastName());
        assert entity.getAvatarUrl().equals(dto.avatarUrl());
        // validate that user have to be null
        assert entity.getUser() == null;
    }

    
    @Test 
    @DisplayName("Test updating UserProfile entity from UserProfile DTO")
    public void testUpdateEntityFromDtoMapping() {
        // Arrange
        Map<String, Object> contact = new HashMap<>();
        Map<String, Object> prefs = new HashMap<>();
        
        UserProfileDto dto = new UserProfileDto(
                null,
                "John",
                "Doe",
                "http://example.com/avatar.jpg",
                contact,
                prefs,
                "john.doe@example.com"
        );

        UserProfile entity = UserProfile.builder()
                .firstName("John")
                .lastName("Smith")
                .avatarUrl("http://example.com/old_avatar.jpg")
                .user(
                    User.builder()
                        .email("john.doe@example.com")
                        .build()
                )
                .build();

        // Act
        mapper.updateEntityFromDto(dto, entity);

        // Assert
        assert entity.getFirstName().equals(dto.firstName());
        assert entity.getLastName().equals(dto.lastName());
        assert entity.getAvatarUrl().equals(dto.avatarUrl());
        // validate that user remains unchanged
        assert entity.getUser().getEmail().equals("john.doe@example.com");
    }

}
