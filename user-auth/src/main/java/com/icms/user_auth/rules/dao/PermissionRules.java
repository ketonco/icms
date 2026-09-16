package com.icms.user_auth.rules.dao;
import com.icms.shared.rules.BaseDaoCatalogRules;
import com.icms.user_auth.entity.Permission;
import com.icms.user_auth.repository.PermissionRepository;
import org.springframework.stereotype.Component;

@Component 
public class PermissionRules extends BaseDaoCatalogRules<Permission, PermissionRepository>{

    public PermissionRules(PermissionRepository repository) {
        super(repository);
    }

}
