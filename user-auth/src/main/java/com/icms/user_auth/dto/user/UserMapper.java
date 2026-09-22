package com.icms.user_auth.dto.user;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.icms.shared.dto.BaseMapper;

import com.icms.shared.config.mapper.MapperSetting;
import com.icms.user_auth.dto.userprofile.UserProfileMapper;
import com.icms.user_auth.entity.User;
@Mapper(
    config = MapperSetting.class,
    builder = @Builder(disableBuilder = true),
    uses = {UserProfileMapper.class}
)
public interface UserMapper extends BaseMapper<User,UserDto> {

    @Mapping(source = "status.name", target = "status")
    UserDto toDto(User user);

    @Mapping(source = "status", target = "status.name")
    User toEntity(UserDto userDto);

    @Mapping(target = "status", ignore = true)
    void updateEntityFromDto(UserDto userDto, @MappingTarget User user);

}
