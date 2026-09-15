package com.icms.user_auth.entity;

import com.icms.shared.entity.BaseCatalogTranslationEntity;
import com.icms.shared.entity.Language;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "usertype_translation")
@NoArgsConstructor 
public class UserTypeTranslation extends BaseCatalogTranslationEntity<UserType>{

    public UserTypeTranslation(UserType catalog, Language language, String translation, String description) {
        super(catalog, language, translation, description);
    }
    
}
