package com.icms.shared.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // Enable auditing for this entity from Spring Data JPA
@Getter
@Setter
public abstract class AuditableEntity<ID> {

     
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;
    
    @CreatedDate
    @Column(updatable = false, nullable = false, name="created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(updatable = true, nullable = true, name="updated_at")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(updatable = false, nullable = false, name="created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(updatable = true, nullable = true, name="updated_by")
    private String updatedBy;

}
