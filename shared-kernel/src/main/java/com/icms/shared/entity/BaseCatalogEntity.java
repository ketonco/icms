package com.icms.shared.entity;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Column;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;


@MappedSuperclass
@Audited
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BaseCatalogEntity extends LongAuditableEntity {

    @Column(unique = true, updatable = true, nullable = false, name="code")
    private String code;

    @Column(updatable = true, nullable = false, name="active")
    private Boolean active;

    @Column(unique = true, updatable = true, nullable = false, name="name", length = 50)
    private String name; 

}
