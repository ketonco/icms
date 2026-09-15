package com.icms.user_auth.cli.level1;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.List;
import com.icms.user_auth.entity.UserStatus;

import com.icms.user_auth.cli.DataSeed;
import com.icms.user_auth.service.daoservice.UserStatusService;


@Component 
@Profile("task")
public class UserStatusDataSeed implements DataSeed{

    private UserStatusService userStatusService;

    public UserStatusDataSeed(UserStatusService userStatusService) {
        this.userStatusService = userStatusService;
    }

    List<UserStatus> userStatusList = List.of(
        new UserStatus("ACT", Boolean.TRUE, "ACTIVE"),
        new UserStatus("INA", Boolean.TRUE, "INACTIVE"),
        new UserStatus("SUS", Boolean.TRUE, "SUSPENDED")
    );

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getName() {
        return "UserStatusDataSeed";
    }

    @Override
    public void run() {
        userStatusList.forEach(this::save);
    }

    private void save(UserStatus userStatus) {
        UserStatus existing = userStatusService.findByCode(userStatus.getCode());
        if (existing != null) {
            existing.setActive(userStatus.getActive());
            existing.setName(userStatus.getName());
            userStatusService.update(existing);
        } else {
            userStatusService.save(userStatus);
        }
    }
}
