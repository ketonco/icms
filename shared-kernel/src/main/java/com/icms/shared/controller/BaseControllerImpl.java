package com.icms.shared.controller;

import com.icms.shared.service.BaseService;
import com.icms.shared.dto.IdentifiableDtoImpl;

public interface BaseControllerImpl<ID, DTO extends IdentifiableDtoImpl<ID>> {

    BaseService<?, DTO, ID, ?> getService();

}
