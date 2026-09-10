package com.icms.shared.entity;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Column;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BaseCatalogEntity extends AuditableEntity<Long> {

    @Column(unique = true, updatable = true, nullable = false, name="code")
    private String code;

    @Column(updatable = true, nullable = false, name="active")
    private Boolean active;
}
