package com.icms.user_auth.rules.dao;

import org.springframework.stereotype.Component;

import com.icms.shared.rules.BaseDaoCatalogTranslationRules;
import com.icms.user_auth.entity.UserTypeTranslation;
import com.icms.user_auth.repository.UserTypeTranslationRepository;
import com.icms.user_auth.entity.UserType;

@Component 
public class UserTypeTranslationRules extends BaseDaoCatalogTranslationRules<UserTypeTranslation, UserTypeTranslationRepository, UserType> {

    public UserTypeTranslationRules(UserTypeTranslationRepository repository) {
        super(repository);
    }

}
