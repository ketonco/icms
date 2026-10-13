package com.icms.user_auth.repository;

import org.springframework.stereotype.Repository;
import com.icms.shared.repository.BaseRepository;
import com.icms.user_auth.entity.EmailOutbox;
    
@Repository 
public interface EmailOutboxRepository extends BaseRepository<EmailOutbox, String>{

}
