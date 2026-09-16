package com.icms.user_auth.cli.level1;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.icms.user_auth.cli.DataSeed;
import com.icms.user_auth.entity.Permission;
import com.icms.user_auth.service.daoservice.PermissionService;

@Component 
@Profile("task")
public class PermissionDataSeed implements DataSeed{

    private PermissionService permissionService;

    public PermissionDataSeed(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    private List<Permission> permissionList = List.of(
        new Permission("READ", Boolean.TRUE, "Read"),
        new Permission("WRITE", Boolean.TRUE, "Write"),
        new Permission("DELETE", Boolean.TRUE, "Delete"),
        new Permission("EXECUTE", Boolean.TRUE, "Execute")
    );

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getName() {
        return "PermissionDataSeed";
    }

    @Override
    public void run() {
        permissionList.forEach(this::save);
    }

    private void save(Permission permission) {
        Permission existing = permissionService.findByCode(permission.getCode());
        if (existing != null) {
            existing.setActive(permission.getActive());
            existing.setName(permission.getName());
            permissionService.update(existing);
        } else {
            permissionService.save(permission);
        }
    }

}
