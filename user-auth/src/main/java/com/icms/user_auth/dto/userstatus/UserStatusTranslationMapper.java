package com.icms.user_auth.dto.userstatus;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.icms.shared.config.mapper.MapperSetting;
import com.icms.shared.dto.BaseMapper;
import com.icms.user_auth.entity.UserStatusTranslation;


@Mapper(
    config = MapperSetting.class,
    builder = @Builder(disableBuilder = true)
)
public interface UserStatusTranslationMapper extends BaseMapper<UserStatusTranslation, UserStatusTranslationDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "catalog.id", source = "catalogId")
    @Mapping(target = "language.id", source = "languageId")
    UserStatusTranslation toEntity(UserStatusTranslationDto dto);

    @Override
    @Mapping(target = "catalogId", source = "catalog.id")
    @Mapping(target = "languageId", source = "language.id")
    UserStatusTranslationDto toDto(UserStatusTranslation entity);
}
