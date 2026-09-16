package com.icms.user_auth.service.daoservice;
import com.icms.shared.service.BaseCatalogService;
import com.icms.user_auth.dto.permission.PermissionDto;
import com.icms.user_auth.dto.permission.PermissionMapper;
import com.icms.user_auth.entity.Permission;
import com.icms.user_auth.repository.PermissionRepository;
import com.icms.user_auth.rules.dao.PermissionRules;

import org.springframework.stereotype.Service;

@Service 
public class PermissionService extends BaseCatalogService<Permission, PermissionDto, PermissionRepository>{

    public PermissionService(PermissionRepository repository, PermissionMapper mapper, PermissionRules rules) {
        super(repository, mapper, rules);
    }

}
