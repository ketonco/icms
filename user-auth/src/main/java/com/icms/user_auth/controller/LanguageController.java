package com.icms.user_auth.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icms.shared.controller.BaseController;
import com.icms.shared.controller.ReadController;
import com.icms.shared.controller.DeleteController;
import com.icms.shared.controller.UpdateController;
import com.icms.shared.controller.WriteController;
import com.icms.shared.dto.RestResponse;
import com.icms.user_auth.dto.language.LanguageDto;
import com.icms.user_auth.service.daoservice.LanguageService;

@RestController
@RequestMapping("/api/v1/auth/languages")
public class LanguageController extends BaseController<Long, LanguageDto> 
    implements ReadController<Long, LanguageDto>,
    WriteController<Long, LanguageDto>,
    UpdateController<Long, LanguageDto>,
    DeleteController<Long, LanguageDto>{

    private final LanguageService service;

    public LanguageController(LanguageService service) {
        super(service);
        this.service = service;
    }

    @GetMapping ("/code/{code}")
    public ResponseEntity<RestResponse<LanguageDto>> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(RestResponse.ok(service.findDtoByCode(code)));
    }

}
