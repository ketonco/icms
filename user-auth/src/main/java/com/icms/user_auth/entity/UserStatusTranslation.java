package com.icms.user_auth.entity;

import com.icms.shared.entity.BaseCatalogTranslationEntity;
import com.icms.shared.entity.Language;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "userstatus_translation")
public class UserStatusTranslation extends BaseCatalogTranslationEntity<UserStatus>{

    public UserStatusTranslation(UserStatus catalog, Language language, String translation, String description) {
        super(catalog, language, translation, description);
    }

}
