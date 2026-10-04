package com.icms.user_auth.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.icms.user_auth.entity.User;
import com.icms.shared.repository.BaseRepository;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Modifying
    @Query(value = "DELETE FROM user_types WHERE user_id = (SELECT id FROM users WHERE username = :username)", nativeQuery = true)
    void deleteUserTypesByUsername(@Param("username") String username);

    @Modifying
    @Query("DELETE FROM User u WHERE u.username = :username")
    void deleteByUsername(@Param("username") String username);

    @Modifying
    @Query(value = "DELETE FROM user_profiles WHERE user_id = (SELECT id FROM users WHERE username = :username)", nativeQuery = true)
    void deleteProfileByUsername(@Param("username") String username);

}
