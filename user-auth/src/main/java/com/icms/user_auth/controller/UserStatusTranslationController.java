package com.icms.user_auth.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;  
import com.icms.shared.controller.BaseController;
import com.icms.shared.controller.ReadController;
import com.icms.shared.controller.WriteController;
import com.icms.shared.controller.UpdateController;
import com.icms.shared.dto.RestResponse;
import com.icms.shared.controller.DeleteController;
import com.icms.user_auth.dto.userstatus.UserStatusTranslationDto;
import com.icms.user_auth.service.daoservice.UserStatusTranslationService;

@RestController 
@RequestMapping("api/v1/auth/user-status-translations")
public class UserStatusTranslationController extends BaseController<Long, UserStatusTranslationDto> 
    implements ReadController<Long, UserStatusTranslationDto>, 
    WriteController<Long, UserStatusTranslationDto>,
    UpdateController<Long, UserStatusTranslationDto>,
    DeleteController<Long, UserStatusTranslationDto> {

    private final UserStatusTranslationService userStatusTranslationService;

    public UserStatusTranslationController(UserStatusTranslationService userStatusTranslationService) {
        super(userStatusTranslationService);
        this.userStatusTranslationService = userStatusTranslationService;
    }

    @GetMapping("/catalog/{catalogId}")
    public ResponseEntity<RestResponse<List<UserStatusTranslationDto>>> findByCatalog(@PathVariable Long catalogId) {

        List<UserStatusTranslationDto> dtos = userStatusTranslationService.findByCatalogId(catalogId);
        return ResponseEntity.ok(RestResponse.ok(dtos));
    }
}
