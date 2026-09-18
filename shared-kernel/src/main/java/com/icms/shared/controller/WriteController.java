package com.icms.shared.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import com.icms.shared.Utils.MessageResolver;
import com.icms.shared.dto.RestResponse;

public interface WriteController<ID, DTO> 
    extends BaseControllerImpl<ID, DTO> {

    @PostMapping
    default ResponseEntity<RestResponse<DTO>> create(@RequestBody DTO dto) {
        DTO createdDto = getService().save(dto);
        return ResponseEntity.ok(RestResponse.ok(createdDto, MessageResolver.resolveMessage("S-000")));
    }

}
