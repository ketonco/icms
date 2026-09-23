package com.icms.user_auth.dto.userstatus;
import com.icms.user_auth.entity.UserStatus;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.icms.shared.config.mapper.MapperSetting;
import com.icms.shared.dto.BaseMapper;

@Mapper(
    config = MapperSetting.class,
    builder = @Builder(disableBuilder = true)
)
public interface UserStatusMapper extends BaseMapper<UserStatus, UserStatusDto>{

    @Override
    @Mapping(target = "id", ignore = true)
    UserStatus toEntity(UserStatusDto dto);

    @Override
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(UserStatusDto dto, @MappingTarget  UserStatus entity);

}
