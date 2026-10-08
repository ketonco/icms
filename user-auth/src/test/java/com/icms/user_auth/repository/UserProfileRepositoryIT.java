package com.icms.user_auth.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import com.icms.shared.config.AuditConfig;
import com.icms.user_auth.entity.User;
import com.icms.user_auth.entity.UserProfile;
import com.icms.user_auth.entity.UserStatus;

import org.instancio.Instancio;
import org.instancio.Select;

@SuppressWarnings ("null")
@DataJpaTest 
@ActiveProfiles("test")
@Import(AuditConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 
public class UserProfileRepositoryIT {

    @Autowired 
    private TestEntityManager testEntityManager;

    @Autowired 
    private UserProfileRepository userProfileRepository;

    UserStatus userStatus;
    User user;

    @BeforeEach 
    public void setUp() {
        userStatus = UserStatus.builder()
            .code("TST")
            .active(true)
            .name("Active test")
            .build();
        testEntityManager.persistAndFlush(userStatus);

        user = Instancio.of(User.class)
            .ignore(Select.field(User::getId))
            .set(Select.field(User::getStatus), userStatus)
            .ignore(Select.field(User::getTypes))
            .ignore(Select.field(User::getProfile))
            .create();
        testEntityManager.persistAndFlush(user);
    }

    @Test 
    @DisplayName("Test for saving user profile and the retrieval of the saved entity successfully by user")
    public void testSaveAndRetrieveUserProfileSuccessfully() {
        // Arrange
        UserProfile userProfile = Instancio.of(UserProfile.class)
            .ignore(Select.field(UserProfile::getId))
            .set(Select.field(UserProfile::getUser), user)
            .ignore(Select.field(UserProfile::getContact)) // Ignore contact field : instancio will not populate it correctly, instead instancio fills it with random Object values
            .ignore(Select.field(UserProfile::getPrefs)) // Ignore prefs field : instancio will not populate it correctly, instead instancio fills it with random Object values
            .create();

        UserProfile savedUserProfile = userProfileRepository.saveAndFlush(userProfile);
        testEntityManager.clear();

        // Act
        UserProfile foundUserProfile = userProfileRepository.findByUser(user).orElse(null);

        // Assert
        assertThat(savedUserProfile).isNotNull();
        assertThat(savedUserProfile.getUser()).isNotNull();
        assertThat(savedUserProfile.getUser().getId()).isEqualTo(user.getId());
        assertThat(foundUserProfile).isNotNull();
        assertThat(foundUserProfile.getUser()).isNotNull();
        assertThat(foundUserProfile.getUser().getId()).isEqualTo(user.getId());
        assertThat(foundUserProfile.getId()).isEqualTo(savedUserProfile.getId());
    }

    @Test 
    @DisplayName ("Test when retrieving a user profile by user that does not exist")
    public void testRetrieveUserProfileByNonExistentUser() {
        // Arrange
        // in before each, we set up a user

        // Act
        UserProfile foundUserProfile = userProfileRepository.findByUser(user).orElse(null);

        // Assert
        assertThat(foundUserProfile).isNull();
    }
    

}
