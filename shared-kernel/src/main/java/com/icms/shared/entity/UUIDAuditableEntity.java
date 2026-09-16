package com.icms.shared.entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import jakarta.persistence.GeneratedValue;

@MappedSuperclass
@Getter
@Setter
public class UUIDAuditableEntity extends AuditableEntity implements IdentifiableImpl<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

}
