package com.icms.user_auth.dto.language;

import com.icms.shared.config.mapper.MapperSetting;
import com.icms.shared.dto.BaseMapper;
import com.icms.shared.entity.Language;
import org.mapstruct.Mapper;
import org.mapstruct.Builder;

@Mapper(
    config = MapperSetting.class,
    builder = @Builder(disableBuilder = true)
)
public interface LanguageMapper extends BaseMapper<Language, LanguageDto> {

}
