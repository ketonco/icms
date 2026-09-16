package com.icms.user_auth.entity;
import com.icms.shared.entity.UUIDAuditableEntity;


import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
public class UserProfile extends UUIDAuditableEntity{

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "avatar_url", nullable = true, length = 255)
    private String avatarUrl;    

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "contact", columnDefinition = "jsonb")
    private Map<String, Object> contact= new HashMap<>();    

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "prefs", columnDefinition = "jsonb")
    private Map<String, Object> prefs= new HashMap<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

}
