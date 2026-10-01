package com.icms.user_auth.entity;

import com.icms.shared.entity.BaseCatalogTranslationEntity;
import com.icms.shared.entity.Language;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.envers.Audited;
@Entity 
@Table(name = "usertype_translation")
@AllArgsConstructor 
@Audited
@Builder  
public class UserTypeTranslation extends BaseCatalogTranslationEntity<UserType>{

    public UserTypeTranslation(UserType catalog, Language language, String translation, String description) {
        super(catalog, language, translation, description);
    }
    
}
