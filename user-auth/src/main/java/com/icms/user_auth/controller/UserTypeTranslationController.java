package com.icms.user_auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import com.icms.shared.controller.BaseController;
import com.icms.shared.controller.DeleteController;
import com.icms.shared.controller.ReadController;
import com.icms.shared.controller.WriteController;
import com.icms.shared.dto.RestResponse;
import com.icms.user_auth.dto.usertype.UserTypeTranslationDto;
import com.icms.user_auth.service.daoservice.UserTypeTranslationService;

@RestController 
@RequestMapping("/api/v1/auth/user-type-translations")
public class UserTypeTranslationController extends BaseController<Long, UserTypeTranslationDto> 
    implements ReadController<Long, UserTypeTranslationDto>, WriteController<Long, UserTypeTranslationDto>, DeleteController<Long, UserTypeTranslationDto> {

    private final UserTypeTranslationService userTypeTranslationService;
    
    public UserTypeTranslationController(UserTypeTranslationService userTypeTranslationService) {
        super(userTypeTranslationService);
        this.userTypeTranslationService = userTypeTranslationService;
    }

    @GetMapping("/catalog/{catalogId}")
    public ResponseEntity<RestResponse<List<UserTypeTranslationDto>>> findByCatalog(@PathVariable Long catalogId) {
        List<UserTypeTranslationDto> dtos = userTypeTranslationService.findByCatalogId(catalogId);
        return ResponseEntity.ok(RestResponse.ok(dtos));
    }


}
