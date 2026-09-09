package com.icms.shared.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "languages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Lombok annotation to generate a builder pattern for this class, this means you can create instances of Language using a fluent API.
public class Language extends BaseCatalogEntity {

    @Column(unique = true, updatable = true, nullable = false, name="name", length = 50)
    private String name; // Ej: English, Spanish, etc.

    @Column(updatable = true, nullable = false, name="is_default")
    private Boolean isDefault; // Indicate if this language is the default one
}
