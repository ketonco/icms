package com.icms.user_auth.dto.permission;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import com.icms.shared.config.mapper.MapperSetting;
import com.icms.shared.dto.BaseMapper;
import com.icms.user_auth.entity.Permission;

@Mapper(
    config = MapperSetting.class,
    builder = @Builder(disableBuilder = true)
)
public interface PermissionMapper extends BaseMapper<Permission, PermissionDto>{

}
