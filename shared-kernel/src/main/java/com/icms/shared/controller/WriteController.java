package com.icms.shared.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.icms.shared.dto.IdentifiableDtoImpl;
import com.icms.shared.dto.RestResponse;

public interface WriteController<ID, DTO extends IdentifiableDtoImpl<ID>> 
    extends BaseControllerImpl<ID, DTO> {

    @PostMapping
    default ResponseEntity<RestResponse<DTO>> create(@RequestBody DTO dto) {
        DTO createdDto = getService().save(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(RestResponse.created(createdDto, "S-001"));
    }

}
