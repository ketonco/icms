package com.icms.user_auth.dto.user;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import com.icms.shared.config.mapper.MapperSetting;
import com.icms.shared.dto.BaseMapper;
import com.icms.user_auth.dto.userprofile.UserProfileMapper;
import com.icms.user_auth.entity.User;
@Mapper(
    config = MapperSetting.class,
    builder = @Builder(disableBuilder = true),
    uses = {UserProfileMapper.class}
)
public interface UserMapper extends BaseMapper<User,UserDto> {

}
