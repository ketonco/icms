package com.icms.shared.dto;

import java.util.List;

import org.mapstruct.MappingTarget;

public interface BaseMapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
    List<D> toDtoList(List<E> entityList);
    List<E> toEntityList(List<D> dtoList);
    
    void updateEntityFromDto(D dto, @MappingTarget E entity);
}
