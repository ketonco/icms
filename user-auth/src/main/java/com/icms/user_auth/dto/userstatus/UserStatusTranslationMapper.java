package com.icms.user_auth.dto.userstatus;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import com.icms.shared.config.mapper.MapperSetting;

import com.icms.shared.dto.BaseMapper;
import com.icms.user_auth.entity.UserStatusTranslation;


@Mapper(
    config = MapperSetting.class,
    builder = @Builder(disableBuilder = true)
)
public interface UserStatusTranslationMapper extends BaseMapper<UserStatusTranslation, UserStatusTranslationDto> {

}
