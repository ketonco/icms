package com.icms.user_auth.entity;

import com.icms.shared.entity.BaseCatalogEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "userstatus")
public class UserStatus extends BaseCatalogEntity{

    public UserStatus() {
        super();
    }
    
    public UserStatus(String code, Boolean active, String description) {
        super(code, active, description);
    }
}
