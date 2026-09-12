package com.icms.user_auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icms.shared.controller.BaseController;
import com.icms.shared.controller.ReadController;
import com.icms.shared.controller.DeleteController;
import com.icms.shared.controller.WriteController;
import com.icms.user_auth.dto.language.LanguageDto;
import com.icms.user_auth.service.daoservice.LanguageService;

@RestController
@RequestMapping("/api/v1/auth/languages")
public class LanguageController extends BaseController<Long, LanguageDto> 
    implements ReadController<Long, LanguageDto>,
    WriteController<Long, LanguageDto>,
    DeleteController<Long, LanguageDto>{

    public LanguageController(LanguageService service) {
        super(service);
    }

}
