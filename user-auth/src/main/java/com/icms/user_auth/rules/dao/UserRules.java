package com.icms.user_auth.rules.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.icms.user_auth.repository.UserRepository;
import com.icms.shared.exceptions.BusinessRuleException;
import com.icms.user_auth.entity.User;

@Component
public class UserRules {

    private UserRepository userRepository; // not used yet

    @Autowired 
    public UserRules(UserRepository userRepository) {
        this.userRepository = userRepository; // field disabled, not used yet
    }

    public void canCreate(User user) {
        uniqueEmail(user);
        uniqueUsername(user);
    }

    private void uniqueEmail(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new BusinessRuleException("Usr-002");
        }
    }

    private void uniqueUsername(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new BusinessRuleException("Usr-003");
        }
    }

}

