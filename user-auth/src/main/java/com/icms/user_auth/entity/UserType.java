package com.icms.user_auth.entity;
import com.icms.shared.entity.BaseCatalogEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "usertypes")
public class UserType extends BaseCatalogEntity{

    public UserType() {
    }

    public UserType(String code, Boolean active, String description) {
        super(code, active, description);
    }

}
