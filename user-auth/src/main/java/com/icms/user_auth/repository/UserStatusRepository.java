package com.icms.user_auth.repository;

import org.springframework.stereotype.Repository;

import com.icms.shared.repository.BaseCatalogRepository;
import com.icms.user_auth.entity.UserStatus;

@Repository 
public interface UserStatusRepository extends BaseCatalogRepository<UserStatus, Long>{

}
