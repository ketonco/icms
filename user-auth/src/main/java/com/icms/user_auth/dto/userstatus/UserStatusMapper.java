package com.icms.user_auth.dto.userstatus;
import com.icms.user_auth.entity.UserStatus;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import com.icms.shared.config.mapper.MapperSetting;
import com.icms.shared.dto.BaseMapper;

@Mapper(
    config = MapperSetting.class,
    builder = @Builder(disableBuilder = true)
)
public interface UserStatusMapper extends BaseMapper<UserStatus, UserStatusDto>{

}
