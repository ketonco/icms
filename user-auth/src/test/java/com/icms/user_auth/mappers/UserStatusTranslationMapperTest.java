package com.icms.user_auth.mappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.assertj.core.api.Assertions.assertThat;
import com.icms.user_auth.dto.userstatus.UserStatusTranslationDto;
import org.instancio.Instancio;
import com.icms.user_auth.entity.UserStatusTranslation;

import com.icms.user_auth.dto.userstatus.UserStatusTranslationMapper;

public class UserStatusTranslationMapperTest {

    private UserStatusTranslationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UserStatusTranslationMapper.class);
    }

    /* test entity to dto when entity has catalog and language objects populated */
    @Test
    @DisplayName("Test mapping from Entity to DTO with catalog and language objects")    
    void mapFromEntityToDto() {
        UserStatusTranslation entity = Instancio.create(UserStatusTranslation.class);

        UserStatusTranslationDto dto = mapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.catalogId()).isEqualTo(entity.getCatalog().getId());
        assertThat(dto.languageId()).isEqualTo(entity.getLanguage().getId());
        assertThat(dto.translation()).isEqualTo(entity.getTranslation());
    }

    /* test dto to entity when dto has catalog_id and language_id but the entity must have the corresponding catalog and language objects populated */
    @Test
    @DisplayName("Test mapping from DTO to Entity with catalog_id and language_id")    
    void createEntityFromDto() {
        UserStatusTranslationDto dto = Instancio.create(UserStatusTranslationDto.class);

        UserStatusTranslation entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getCatalog()).isNull();
        assertThat(entity.getLanguage()).isNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getTranslation()).isEqualTo(dto.translation());
    }

    @Test
    @DisplayName("Test updating an existing Entity from DTO")
    void updateEntityFromDto() {    
        UserStatusTranslation existingEntity = Instancio.create(UserStatusTranslation.class);
        Long existingCatalogId = existingEntity.getCatalog().getId();
        Long existingLanguageId = existingEntity.getLanguage().getId();
        Long existingUserStatusTranslationId = existingEntity.getId();


        UserStatusTranslationDto dto = Instancio.create(UserStatusTranslationDto.class);

        mapper.updateEntityFromDto(dto, existingEntity);

        assertThat(existingEntity).isNotNull();
        assertThat(existingEntity.getTranslation()).isEqualTo(dto.translation());
        // Ensure that the catalog, language, and ID remain unchanged after the update
        assertThat(existingEntity.getCatalog().getId()).isEqualTo(existingCatalogId);
        assertThat(existingEntity.getLanguage().getId()).isEqualTo(existingLanguageId);
        assertThat(existingEntity.getId()).isEqualTo(existingUserStatusTranslationId);
    }

}
