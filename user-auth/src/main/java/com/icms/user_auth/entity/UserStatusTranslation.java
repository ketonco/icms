package com.icms.user_auth.entity;

import com.icms.shared.entity.BaseCatalogTranslationEntity;
import com.icms.shared.entity.Language;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "userstatus_translation")
@AllArgsConstructor 
@Builder 
public class UserStatusTranslation extends BaseCatalogTranslationEntity<UserStatus>{

    public UserStatusTranslation(UserStatus catalog, Language language, String translation, String description) {
        super(catalog, language, translation, description);
    }

}
