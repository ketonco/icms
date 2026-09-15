package com.icms.user_auth.repository;

import org.springframework.stereotype.Repository;

import com.icms.shared.repository.BaseCatalogRepository;
import com.icms.user_auth.entity.UserType;

@Repository 
public interface UserTypeRepository extends BaseCatalogRepository<UserType, Long> {

}
