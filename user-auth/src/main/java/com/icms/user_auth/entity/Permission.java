package com.icms.user_auth.entity;

import com.icms.shared.entity.BaseCatalogEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permissions")
@NoArgsConstructor 
public class Permission extends BaseCatalogEntity{

    public Permission(String code, Boolean active, String description) {
        super(code, active, description);
    }

}
