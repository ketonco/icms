package com.icms.shared.controller;
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
        List<DTO> dtos = getService().findAllDto();
        return ResponseEntity.ok(RestResponse.ok(dtos));
    }

    @GetMapping("/{id}")
    default ResponseEntity<RestResponse<DTO>> getById(@PathVariable ID id) {
        DTO dto = getService().findDtoById(id);
        return ResponseEntity.ok(RestResponse.ok(dto));
    }

}
