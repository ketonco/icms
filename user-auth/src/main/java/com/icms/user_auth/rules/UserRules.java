package com.icms.user_auth.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.icms.user_auth.repository.UserRepository;

@Component
public class UserRules {

    // private UserRepository userRepository; // not used yet

    @Autowired 
    public UserRules(UserRepository userRepository) {
        // this.userRepository = userRepository; // field disabled, not used yet
    }

}

