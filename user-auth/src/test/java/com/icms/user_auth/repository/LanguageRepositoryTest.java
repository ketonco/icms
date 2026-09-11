package com.icms.user_auth.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Import;
import com.icms.shared.config.AuditConfig;
import com.icms.shared.entity.Language;

@DataJpaTest 
@ActiveProfiles("test")
@Import(AuditConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 
public class LanguageRepositoryTest {

    @Autowired 
    private TestEntityManager testEntityManager;

    @Autowired 
    private LanguageRepository languageRepository;

    @Test 
    @DisplayName("Test for saving and retrieving a language entity")
    public void testSaveAndRetrieveLanguage() {
        // Given
        //new Language("en-US", "English", Boolean.TRUE, Boolean.TRUE)
        Language language = new Language();
        language.setName("English test");
        language.setCode("en-USA");
        language.setActive(Boolean.FALSE);
        language.setIsDefault(Boolean.FALSE);
        
        // action: persist the language entity
        testEntityManager.persistAndFlush(language);
        
        // When
        Language found = languageRepository.findByCode(language.getCode()).orElse(null);

        // Then
        assertNotNull(found);
        assertEquals(language.getName(), found.getName());
        assertEquals(language.getCode(), found.getCode());
        assertEquals(language.getActive(), found.getActive());
        assertEquals(language.getIsDefault(), found.getIsDefault());
    }

}
