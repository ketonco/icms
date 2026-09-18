package com.icms.user_auth.repository;

import java.util.UUID;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.icms.user_auth.entity.User;
import com.icms.user_auth.entity.UserProfile;
import com.icms.shared.repository.BaseRepository;

@Repository
public interface UserProfileRepository extends BaseRepository<UserProfile, UUID>{

    Optional<UserProfile> findByUser(User user);

}
