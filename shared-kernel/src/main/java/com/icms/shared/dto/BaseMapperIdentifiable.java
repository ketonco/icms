package com.icms.shared.dto;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

public interface BaseMapperIdentifiable<E, D> extends BaseMapper<E, D>{
    @Override
    @Mapping (target = "id", ignore = true)
    E toEntity(D dto);

    @Mapping (target = "id", ignore = true)
    void updateEntityFromDto(D dto, @MappingTarget E entity);
}
