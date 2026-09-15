package com.icms.user_auth.mappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.icms.user_auth.dto.userstatus.UserStatusTranslationDto;
import com.icms.user_auth.entity.UserStatus;
import com.icms.shared.entity.Language;
import com.icms.user_auth.entity.UserStatusTranslation;

import com.icms.user_auth.dto.userstatus.UserStatusTranslationMapper;

public class UserStatusTranslationMapperTest {

    private UserStatusTranslationMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(UserStatusTranslationMapper.class);
    }

    /* test dto to entity when dto has catalog_id and language_id but the entity must have the corresponding catalog and language objects populated */
    @Test
    @DisplayName("Test mapping from DTO to Entity with catalog_id and language_id")    
    public void testDtoToEntityMapping() {
        UserStatusTranslationDto dto = new UserStatusTranslationDto(null, 1L, 1L, "Some translation", null);

        UserStatusTranslation entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertNotNull(entity.getCatalog());
        assertNotNull(entity.getLanguage());
        assertEquals(dto.catalogId(), entity.getCatalog().getId());
        assertEquals(dto.languageId(), entity.getLanguage().getId());
    }

    /* test entity to dto when entity has catalog and language objects populated */
    @Test
    @DisplayName("Test mapping from Entity to DTO with catalog and language objects")    
    public void testEntityToDtoMapping() {
        UserStatusTranslation entity = new UserStatusTranslation();
        entity.setCatalog(new UserStatus());
        entity.getCatalog().setId(1L);
        entity.setLanguage(new Language());
        entity.getLanguage().setId(1L);
        entity.setTranslation("Some translation");

        UserStatusTranslationDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getCatalog().getId(), dto.catalogId());
        assertEquals(entity.getLanguage().getId(), dto.languageId());
        assertEquals(entity.getTranslation(), dto.translation());
    }

}
