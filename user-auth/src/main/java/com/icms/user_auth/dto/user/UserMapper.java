package com.icms.user_auth.dto.user;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.icms.shared.config.mapper.MapperSetting;
import com.icms.user_auth.dto.userprofile.UserProfileMapper;
import com.icms.user_auth.dto.userstatus.UserStatusMapper;
import com.icms.user_auth.dto.usertype.UserTypeMapper;
import com.icms.user_auth.entity.User;
@Mapper(
    config = MapperSetting.class,
    builder = @Builder(disableBuilder = true),
    uses = {UserProfileMapper.class, UserTypeMapper.class, UserStatusMapper.class}
)
public interface UserMapper{

    CreateUserDto toCreateUserDto(User user);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "types", ignore = true)
    User toEntity(CreateUserDto userDto);

    /* @Mapping(target = "status", ignore = true)
    @Mapping(target = "types", ignore = true)
    void updateEntityFromDto(CreateUserDto userDto, @MappingTarget User user); */

}
