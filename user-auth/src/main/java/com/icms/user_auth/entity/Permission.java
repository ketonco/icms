package com.icms.user_auth.entity;

import com.icms.shared.entity.BaseCatalogEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "permissions")
@AllArgsConstructor
@Builder
public class Permission extends BaseCatalogEntity{

    public Permission(String code, Boolean active, String name) {
        super(code, active, name);
    }

}
