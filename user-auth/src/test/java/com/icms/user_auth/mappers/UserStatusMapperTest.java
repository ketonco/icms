package com.icms.user_auth.mappers;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        UserStatus userStatus = new UserStatus();
        userStatus.setCode("ACT");
        userStatus.setActive(true);
        userStatus.setName("ACTIVE");

        // When
        // You would call the mapper to convert the entity to a DTO
        UserStatusDto userStatusDto = userStatusMapper.toDto(userStatus);

        // Then
        // You would assert that the DTO has the expected values
        assertNotNull(userStatusDto);
        assertEquals("ACT", userStatusDto.code());
        assertEquals(true, userStatusDto.active());
        assertEquals("ACTIVE", userStatusDto.name());
    }

    @Test 
    @DisplayName ("Test if the UserStatusMapper returns entity correctly when mapping from dto")
    public void mapFromDtoToEntity() {
        // Given
        UserStatusDto userStatusDto = new UserStatusDto(Long.valueOf(1), "ACT", "ACTIVE", true);

        // When
        UserStatus userStatus = userStatusMapper.toEntity(userStatusDto);

        // Then
        assertNotNull(userStatus);
        assertEquals(Long.valueOf(1), userStatus.getId());
        assertEquals("ACT", userStatus.getCode());
        assertEquals(true, userStatus.getActive());
        assertEquals("ACTIVE", userStatus.getName());
    }

}
