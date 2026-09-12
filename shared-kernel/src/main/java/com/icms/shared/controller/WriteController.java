package com.icms.shared.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import com.icms.shared.Utils.MessageResolver;
import com.icms.shared.dto.RestResponse;

public interface WriteController<ID, DTO> 
    extends BaseControllerImpl<ID, DTO> {

    @PostMapping
    default ResponseEntity<RestResponse<DTO>> create(@RequestBody DTO dto) {
        DTO createdDto = getService().saveDto(dto);
        return ResponseEntity.ok(RestResponse.ok(createdDto, MessageResolver.resolveMessage("S-000")));
    }

    @PutMapping
    default ResponseEntity<RestResponse<DTO>> update(@RequestBody DTO dto) {
        DTO updatedDto = getService().updateDto(dto);
        return ResponseEntity.ok(RestResponse.ok(updatedDto, MessageResolver.resolveMessage("S-001")));
    }

}
