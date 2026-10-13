package com.icms.user_auth.entity;

import java.time.LocalDateTime;

import org.hibernate.envers.Audited;

import com.icms.shared.entity.UUIDAuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "email_outbox")
@Audited 
@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor
@Builder 
public class EmailOutbox extends UUIDAuditableEntity{

    @Column(name = "recipient", nullable = false, length = 100)
    private String recipient;

    @Column(name = "subject", nullable = false, length = 200)
    private String subject;

    @Column(name = "template", nullable = false, length = 200)
    private String template;

    @Column(name = "status", nullable = false, length = 20) // e.g., "PENDING", "SENT", "FAILED"
    private String status;

    @Column(name = "attempts", nullable = false)
    private int attempts;

    @Column(name = "last_error", length = 500)
    private String lastError;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

}
