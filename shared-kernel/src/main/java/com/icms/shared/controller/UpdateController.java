package com.icms.shared.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.icms.shared.Utils.MessageResolver;
import com.icms.shared.dto.RestResponse;

public interface UpdateController <ID, DTO> 
    extends BaseControllerImpl<ID, DTO> {

    @PutMapping
    default ResponseEntity<RestResponse<DTO>> update(@RequestBody DTO dto) {
        DTO updatedDto = getService().updateDto(dto);
        return ResponseEntity.ok(RestResponse.ok(updatedDto, MessageResolver.resolveMessage("S-001")));
    }
    
}
