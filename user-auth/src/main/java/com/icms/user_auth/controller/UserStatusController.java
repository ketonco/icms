package com.icms.user_auth.controller;
import com.icms.user_auth.dto.userstatus.UserStatusDto;
import com.icms.user_auth.service.daoservice.UserStatusService;
import com.icms.shared.controller.BaseController;
import com.icms.shared.controller.ReadController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("api/v1/auth/user-status")
public class UserStatusController extends BaseController<Long, UserStatusDto> 
    implements ReadController<Long, UserStatusDto>{

    UserStatusService userStatusService;

    public UserStatusController(UserStatusService userStatusService) {
        super(userStatusService);
        this.userStatusService = userStatusService;
    }

}
