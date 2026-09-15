package com.icms.user_auth.dto.usertype;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import com.icms.shared.config.mapper.MapperSetting;
import com.icms.shared.dto.BaseMapper;
import com.icms.user_auth.entity.UserType;

@Mapper(
    config = MapperSetting.class,
    builder = @Builder(disableBuilder = true)
)
public interface UserTypeMapper extends BaseMapper<UserType, UserTypeDto>{

}
