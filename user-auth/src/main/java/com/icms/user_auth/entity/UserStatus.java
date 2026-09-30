package com.icms.user_auth.entity;

import com.icms.shared.entity.BaseCatalogEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "userstatus")
@AllArgsConstructor 
@Builder 
public class UserStatus extends BaseCatalogEntity{
    
    public UserStatus(String code, Boolean active, String name) {
        super(code, active, name);
    }
}
