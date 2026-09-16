package com.icms.user_auth.repository;

import com.icms.user_auth.entity.Permission;

import org.springframework.stereotype.Repository;

import com.icms.shared.repository.BaseCatalogRepository;

@Repository 
public interface PermissionRepository extends BaseCatalogRepository<Permission, Long>{
    
}
