package com.icms.user_auth.repository;

import org.springframework.stereotype.Repository;

import com.icms.shared.repository.BaseCatalogTranslationRepository;
import com.icms.user_auth.entity.UserTypeTranslation;
import com.icms.user_auth.entity.UserType;

@Repository 
public interface UserTypeTranslationRepository extends BaseCatalogTranslationRepository<UserTypeTranslation, UserType>{

}
