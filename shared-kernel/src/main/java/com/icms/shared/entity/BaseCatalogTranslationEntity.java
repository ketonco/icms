package com.icms.shared.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseCatalogTranslationEntity<C> extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Many-to-one relationship with the catalog entity, FetchType.LAZY ensures the catalog is loaded only when accessed
    @JoinColumn(updatable = false, nullable = false, name="catalog_id")
    private C catalog; // Reference to the catalog entity this translation belongs to

    @Column(updatable = false, nullable = false, name="language_id")
    private Long languageId; // Reference to the language entity for this translation

    @Column(updatable = true, nullable = false, name="translation")
    private String translation; // The actual translation text for the catalog entity in the specified language

    @Column(updatable = true, nullable = true, name="description")
    private String description; // Optional description for the translation

}
