package com.icms.user_auth.service.daoservice;
import com.icms.user_auth.dto.userstatus.UserStatusTranslationDto;
import com.icms.user_auth.entity.UserStatus;
import org.springframework.stereotype.Service;
import com.icms.user_auth.dto.userstatus.UserStatusTranslationMapper;

import com.icms.shared.service.BaseCatalogTranslationService;
import com.icms.user_auth.entity.UserStatusTranslation;
import com.icms.user_auth.repository.UserStatusTranslationRepository;
import com.icms.user_auth.rules.UserStatusTranslationRules;

@Service 
public class UserStatusTranslationService extends BaseCatalogTranslationService<UserStatusTranslation, 
                                                                                UserStatusTranslationDto, 
                                                                                UserStatusTranslationRepository,
                                                                                UserStatus>{
    
    public UserStatusTranslationService(UserStatusTranslationRepository repository, UserStatusTranslationMapper mapper, UserStatusTranslationRules rules) {
        super(repository, mapper, rules);
    }

}
