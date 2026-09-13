package com.icms.user_auth.service.daoservice;

import org.springframework.stereotype.Service;
import com.icms.shared.service.BaseCatalogService;
import com.icms.user_auth.dto.userstatus.UserStatusDto;
import com.icms.user_auth.entity.UserStatus;
import com.icms.user_auth.repository.UserStatusRepository;
import com.icms.user_auth.dto.userstatus.UserStatusMapper;
import com.icms.user_auth.rules.dao.UserStatusRules;

@Service 
public class UserStatusService extends BaseCatalogService<UserStatus, UserStatusDto, UserStatusRepository> {

    public UserStatusService(UserStatusRepository repository, UserStatusMapper mapper, UserStatusRules rules) {
        super(repository, mapper, rules);
    }

}
