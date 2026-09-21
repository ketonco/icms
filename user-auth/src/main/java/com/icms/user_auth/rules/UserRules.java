package com.icms.user_auth.rules;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.icms.user_auth.repository.UserRepository;

@Component 
public class UserRules {

    private UserRepository userRepository;

    @Autowired 
    public UserRules(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}

