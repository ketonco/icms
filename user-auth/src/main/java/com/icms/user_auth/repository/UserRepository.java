package com.icms.user_auth.repository;

import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.icms.user_auth.entity.User;
import com.icms.shared.repository.BaseRepository;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByUserNameAndPassword(String username, String password);

}
