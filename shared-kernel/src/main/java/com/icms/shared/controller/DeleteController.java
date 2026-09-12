package com.icms.shared.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.icms.shared.dto.RestResponse;
import com.icms.shared.Utils.MessageResolver;
    
public interface DeleteController<ID, DTO> 
    extends BaseControllerImpl<ID, DTO> {

    @DeleteMapping("/{id}")
    default ResponseEntity<RestResponse<String>> delete(@PathVariable ID id) {
        getService().deleteById(id);
        return ResponseEntity.ok(RestResponse.ok("", MessageResolver.resolveMessage("S-002")));
    }

}
