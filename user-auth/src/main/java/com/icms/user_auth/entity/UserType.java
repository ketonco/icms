package com.icms.user_auth.entity;
import com.icms.shared.entity.BaseCatalogEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.envers.Audited;
@Entity
@Table(name = "usertypes")
@AllArgsConstructor 
@Audited
@Builder 
public class UserType extends BaseCatalogEntity{

    public UserType(String code, Boolean active, String name) {
        super(code, active, name);
    }

}
