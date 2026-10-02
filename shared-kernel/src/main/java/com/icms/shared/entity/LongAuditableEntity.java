package com.icms.shared.entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.MappedSuperclass;
import org.hibernate.envers.Audited;

@MappedSuperclass
@Audited
@Getter
@Setter
public class LongAuditableEntity extends AuditableEntity implements IdentifiableImpl<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

}
