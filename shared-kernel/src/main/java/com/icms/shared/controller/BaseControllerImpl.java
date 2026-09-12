package com.icms.shared.controller;

import com.icms.shared.service.BaseService;

public interface BaseControllerImpl<ID, DTO> {

    BaseService<?, DTO, ID, ?> getService();

}
