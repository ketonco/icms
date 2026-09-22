package com.icms.user_auth.dto.usertype;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.icms.shared.config.mapper.MapperSetting;
import com.icms.shared.dto.BaseMapper;
import com.icms.user_auth.entity.UserTypeTranslation;

@Mapper(
    config = MapperSetting.class,
    builder = @Builder(disableBuilder = true)
)
public interface UserTypeTranslationMapper extends BaseMapper<UserTypeTranslation, UserTypeTranslationDto>{

    @Override
    @Named("toEntityWithoutId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "catalog.id", source = "catalogId")
    @Mapping(target = "language.id", source = "languageId")
    UserTypeTranslation toEntity(UserTypeTranslationDto dto);

    @Override
    @Mapping (target = "catalogId", source = "catalog.id")
    @Mapping(target = "languageId", source = "language.id")
    UserTypeTranslationDto toDto(UserTypeTranslation entity);
    
}
