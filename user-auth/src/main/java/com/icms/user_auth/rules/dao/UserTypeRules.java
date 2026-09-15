package com.icms.user_auth.rules.dao;
import org.springframework.stereotype.Component;
import com.icms.user_auth.entity.UserType;
import com.icms.user_auth.repository.UserTypeRepository;

import com.icms.shared.rules.BaseDaoCatalogRules;

@Component
public class UserTypeRules extends BaseDaoCatalogRules<UserType, UserTypeRepository>{

    public UserTypeRules(UserTypeRepository repository) {
        super(repository);
    }

}
