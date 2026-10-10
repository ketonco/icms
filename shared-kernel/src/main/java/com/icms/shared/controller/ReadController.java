package com.icms.shared.controller;
import java.util.Optional;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import com.icms.shared.dto.IdentifiableDtoImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.icms.shared.dto.RestResponse;

public interface ReadController<ID, DTO extends IdentifiableDtoImpl<ID>> 
    extends BaseControllerImpl<ID, DTO> {

    @GetMapping
    default ResponseEntity<RestResponse<List<DTO>>> getAll() {
        return Optional.ofNullable(getService().findAllDto()).
        filter(dtos -> !dtos.isEmpty())
        .map(dtos -> ResponseEntity.ok(RestResponse.ok(dtos)))
        .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    default ResponseEntity<RestResponse<DTO>> getById(@PathVariable ID id) {
        return Optional.ofNullable(getService().findDtoById(id))
        .map(dto -> ResponseEntity.ok(RestResponse.ok(dto)))
        .orElse(ResponseEntity.noContent().build());
    }

}
