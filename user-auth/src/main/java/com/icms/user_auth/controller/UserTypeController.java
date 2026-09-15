package com.icms.user_auth.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.icms.user_auth.dto.usertype.UserTypeDto;
import com.icms.user_auth.service.daoservice.UserTypeService;
import com.icms.shared.controller.BaseController;
import com.icms.shared.controller.ReadController;
@RestController 
@RequestMapping("api/v1/auth/user-type")
public class UserTypeController extends BaseController<Long, UserTypeDto>
    implements ReadController<Long, UserTypeDto>{

    public UserTypeController(UserTypeService userTypeService) {
        super(userTypeService);
    }

}
