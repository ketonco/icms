package com.icms.user_auth.cli.level1;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.icms.user_auth.cli.DataSeed;
import com.icms.user_auth.entity.UserType;
import com.icms.user_auth.service.daoservice.UserTypeService;

@Component 
@Profile("task")
public class UserTypeDataSeed implements DataSeed{

    private UserTypeService userTypeService;

    public UserTypeDataSeed(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }

    List<UserType> userTypeList = List.of(
        new UserType("ADM", Boolean.TRUE, "ADMIN"),
        new UserType("USR", Boolean.TRUE, "USER"),
        new UserType("MOD", Boolean.TRUE, "MODERATOR"),
        new UserType("GUE", Boolean.TRUE, "GUEST")
    );

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getName() {
        return "UserTypeDataSeed";
    }

    @Override
    public void run() {
        userTypeList.forEach(this::save);
    }

    private void save(UserType userType) {
        UserType existing = userTypeService.findByCode(userType.getCode());
        if (existing != null) {
            existing.setActive(userType.getActive());
            existing.setName(userType.getName());
            userTypeService.update(existing);
        } else {
            userTypeService.save(userType);
        }
    }

}
