package com.icms.user_auth.repository;

import org.springframework.stereotype.Repository;

import com.icms.shared.repository.BaseRepository;
import com.icms.user_auth.entity.ConfirmationToken;

@Repository 
public interface ConfirmationTokenRepository extends BaseRepository<ConfirmationToken, String>{

}
