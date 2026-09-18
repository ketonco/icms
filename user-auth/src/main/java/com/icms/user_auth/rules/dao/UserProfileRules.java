package com.icms.user_auth.rules.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.UUID;

import com.icms.shared.rules.BaseDaoRules;
import com.icms.user_auth.entity.UserProfile;
import com.icms.user_auth.repository.UserProfileRepository;
import com.icms.user_auth.repository.UserRepository;
import com.icms.shared.exceptions.BusinessRuleException;

@Component 
public class UserProfileRules extends BaseDaoRules<UserProfile, UserProfileRepository, UUID> {

    private UserRepository userRepository;
    private UserProfileRepository repository; 

    @Autowired 
    public UserProfileRules(UserProfileRepository repository, UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
        this.repository = repository;
    }

    @Override 
    public void canSave(UserProfile entity) {
        super.canSave(entity);
        checkUserIntegrity(entity);
    }

    @Override 
    public void canUpdate(UserProfile entity) {
        super.canUpdate(entity);
        checkUserIntegrity(entity);
        validSameUser(entity);
    }

    /**
     * Checks the integrity of the associated User in the UserProfile entity.
     * Ensures that the UserProfile is linked to a valid User.
     * 
     * @param entity The UserProfile entity to check.
     */
    private void checkUserIntegrity(UserProfile entity) {
        if (entity.getUser() == null || !userRepository.existsById(entity.getUser().getId())) {
            throw new BusinessRuleException("UsrProf-009");
        }
    }

    /**
     * Validates that the UserProfile is associated with the same User as the persisted record.
     * 
     * @param entity The UserProfile entity to validate.
     */
    private void validSameUser(UserProfile entity) {
        UserProfile persistedProfile = repository.findById(entity.getId())
            .orElseThrow(() -> new BusinessRuleException("UsrProf-001"));

        UUID persistedUserId = persistedProfile.getUser().getId();
        UUID requestedUserId = entity.getUser().getId();

        if (!persistedUserId.equals(requestedUserId)) {
            throw new BusinessRuleException("UsrProf-010");
        }
    }
}
