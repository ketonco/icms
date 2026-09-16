package com.icms.user_auth.entity;

import jakarta.persistence.Table;
import com.icms.shared.entity.UUIDAuditableEntity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import java.util.Set;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

import org.hibernate.envers.AuditJoinTable;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends UUIDAuditableEntity{

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255) 
    private String password;

    @Column(nullable = false) 
    private boolean enabled;

    @Column(nullable = false) 
    private boolean locked;

    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userstatus_id", nullable = false)
    private UserStatus status;

    @OneToOne(
        mappedBy = "user",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private UserProfile profile;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_types",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    @AuditJoinTable(name = "user_types_aud")
    private Set<UserType> types; 

    public void setProfile(UserProfile profile) {
        /*
         * Keeps the bidirectional relationship between User and UserProfile synchronized.
         * If null is received, it means that the current profile is being unlinked;
         * therefore, the inverse reference on the existing profile is also cleared
         * to prevent it from continuing to point to this user. If a valid profile is
         * received, its inverse reference is set to this user. Finally, the profile
         * property of this entity is updated with the new value.
         * This keeps both sides of the association consistent and prevents stale
         * in-memory references while managing the entity.
         */
        if (profile == null) {
            if (this.profile != null) {
                this.profile.setUser(null);
            }
        } else {
            profile.setUser(this);
        }
        this.profile = profile;
    }

}
