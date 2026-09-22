package com.icms.user_auth.dto.userprofile;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.icms.shared.config.mapper.MapperSetting;
import com.icms.shared.dto.BaseMapper;
import com.icms.user_auth.entity.UserProfile;

@Mapper(
    config = MapperSetting.class,
    builder = @Builder(disableBuilder = true)
)
public interface UserProfileMapper extends BaseMapper<UserProfile, UserProfileDto> {

    @Override 
    @Mapping(target = "email", source = "user.email")
    UserProfileDto toDto(UserProfile entity);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserProfile toEntity(UserProfileDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(UserProfileDto dto, @MappingTarget UserProfile entity);

}
