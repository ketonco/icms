package com.icms.user_auth.dto.userstatus;
import com.icms.user_auth.entity.UserStatus;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import com.icms.shared.config.mapper.MapperSetting;
import com.icms.shared.dto.BaseMapperIdentifiable;

@Mapper(
    config = MapperSetting.class,
    builder = @Builder(disableBuilder = true)
)
public interface UserStatusMapper extends BaseMapperIdentifiable<UserStatus, UserStatusDto>{

}
