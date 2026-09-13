package com.icms.user_auth.rules.dao;
import org.springframework.stereotype.Component;
import com.icms.shared.rules.BaseDaoCatalogRules;
import com.icms.user_auth.repository.UserStatusRepository;
import com.icms.user_auth.entity.UserStatus;

@Component
public class UserStatusRules extends BaseDaoCatalogRules<UserStatus, UserStatusRepository> {

    public UserStatusRules(UserStatusRepository repository) {
        super(repository);
    }

}
