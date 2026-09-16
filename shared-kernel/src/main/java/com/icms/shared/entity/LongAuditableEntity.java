package com.icms.shared.entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public class LongAuditableEntity extends AuditableEntity implements IdentifiableImpl<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

}
