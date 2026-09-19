package com.icms.shared.controller;

import com.icms.shared.dto.IdentifiableDtoImpl;
import com.icms.shared.service.BaseService;

public class BaseController<ID, DTO extends IdentifiableDtoImpl<ID>> 
    implements BaseControllerImpl<ID, DTO> {

    protected BaseService<?, DTO, ID, ?> service;

    public BaseController(BaseService<?, DTO, ID, ?> service) {
        this.service = service;
    }

    @Override
    public BaseService<?, DTO, ID, ?> getService() {
        return service; // Implementación concreta en subclases
    }
}
