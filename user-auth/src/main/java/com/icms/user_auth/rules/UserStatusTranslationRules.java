package com.icms.user_auth.rules;

import org.springframework.stereotype.Component;

import com.icms.user_auth.entity.UserStatus;
import com.icms.user_auth.entity.UserStatusTranslation;
import com.icms.shared.rules.BaseDaoCatalogTranslationRules;
import com.icms.user_auth.repository.UserStatusTranslationRepository;

@Component 
public class UserStatusTranslationRules extends BaseDaoCatalogTranslationRules<UserStatusTranslation, UserStatusTranslationRepository, UserStatus>{

    public UserStatusTranslationRules(UserStatusTranslationRepository repository) {
        super(repository);
    }

}
