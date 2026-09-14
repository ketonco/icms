package com.icms.user_auth.repository;

import org.springframework.stereotype.Repository;
import com.icms.shared.repository.BaseCatalogTranslationRepository;
import com.icms.user_auth.entity.UserStatus;
import com.icms.user_auth.entity.UserStatusTranslation;

@Repository 
public interface UserStatusTranslationRepository extends BaseCatalogTranslationRepository<UserStatusTranslation, UserStatus>{

}
