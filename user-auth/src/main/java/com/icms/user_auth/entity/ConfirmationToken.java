package com.icms.user_auth.entity;

import java.sql.Timestamp;

import org.hibernate.envers.Audited;

import com.icms.shared.entity.UUIDAuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "confirmation_tokens")
@Audited 
@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor
@Builder 
public class ConfirmationToken extends UUIDAuditableEntity{

    @Column(name = "token", nullable = false, length = 60)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expire_at", nullable = false)
    private Timestamp expireAt;
}
