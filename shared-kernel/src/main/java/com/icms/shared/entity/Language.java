package com.icms.shared.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;


@Entity 
@Table(name = "languages")@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Lombok annotation to generate a builder pattern for this class, this means you can create instances of Language using a fluent API.
public class Language extends BaseCatalogEntity {

    public Language(String code, String name, Boolean isDefault, Boolean active) {
        this.name = name;
        this.isDefault = isDefault;
        super.setActive(active);
        super.setCode(code);
    }

    public Language(Long id, String code, String name, Boolean isDefault, Boolean active) {
        this(code, name, isDefault, active);
        super.setId(id);
    }

    @Column(unique = true, updatable = true, nullable = false, name="name", length = 50)
    private String name; // Ej: English, Spanish, etc.

    @Column(updatable = true, nullable = false, name="is_default")
    private Boolean isDefault; // Indicate if this language is the default one
}
