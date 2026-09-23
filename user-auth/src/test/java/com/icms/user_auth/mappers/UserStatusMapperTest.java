package com.icms.user_auth.mappers;

import org.instancio.Instancio;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import com.icms.user_auth.dto.userstatus.UserStatusMapper;
import com.icms.user_auth.entity.UserStatus;
import com.icms.user_auth.dto.userstatus.UserStatusDto;

public class UserStatusMapperTest {

    private UserStatusMapper userStatusMapper;

    @BeforeEach
    public void setUp() {
        userStatusMapper = Mappers.getMapper(UserStatusMapper.class);
    }

    @Test 
    @DisplayName ("Test if the UserStatusMapper returns dto correctly when mapping from entity")
    public void mapFromEntityToDto() {
        // Given
        UserStatus userStatus = Instancio.create(UserStatus.class);

        // When
        // You would call the mapper to convert the entity to a DTO
        UserStatusDto userStatusDto = userStatusMapper.toDto(userStatus);

        // Then
        // You would assert that the DTO has the expected values
        assertThat(userStatusDto).isNotNull();
        assertThat(userStatusDto.code()).isEqualTo(userStatus.getCode());
        assertThat(userStatusDto.active()).isEqualTo(userStatus.getActive());
        assertThat(userStatusDto.name()).isEqualTo(userStatus.getName());
    }

    @Test 
    @DisplayName ("Test if the UserStatusMapper returns entity correctly when mapping from dto")
    public void createEntityFromDto() {
        // Given
        UserStatusDto userStatusDto = Instancio.create(UserStatusDto.class);

        // When
        UserStatus userStatus = userStatusMapper.toEntity(userStatusDto);

        // Then
        assertThat(userStatus).isNotNull();
        assertThat(userStatus.getCode()).isEqualTo(userStatusDto.code());
        assertThat(userStatus.getActive()).isEqualTo(userStatusDto.active());
        assertThat(userStatus.getName()).isEqualTo(userStatusDto.name());
        // The ID should be null because it is ignored during mapping from DTO to entity for the entity creation.
        assertThat(userStatus.getId()).isNull();
    }

    @Test 
    @DisplayName ("Test if the UserStatusMapper updates entity correctly when mapping from dto")
    void updateEntityFromDto() {
        // Given
        UserStatus existingUserStatus = Instancio.create(UserStatus.class);
        Long existingId = existingUserStatus.getId();

        UserStatusDto userStatusDto = Instancio.create(UserStatusDto.class);

        // When
        userStatusMapper.updateEntityFromDto(userStatusDto, existingUserStatus);

        // Then
        assertThat(existingUserStatus.getCode()).isEqualTo(userStatusDto.code());
        assertThat(existingUserStatus.getName()).isEqualTo(userStatusDto.name());
        assertThat(existingUserStatus.getActive()).isEqualTo(userStatusDto.active());
        // The ID should remain unchanged after the update
        assertThat(existingUserStatus.getId()).isEqualTo(existingId);
    }
}
