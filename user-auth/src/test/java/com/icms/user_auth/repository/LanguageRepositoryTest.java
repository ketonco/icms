package com.icms.user_auth.repository;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
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
class LanguageRepositoryTest {

    @Autowired 
    private TestEntityManager testEntityManager;

    @Autowired 
    private LanguageRepository languageRepository;

    @Test 
    @SuppressWarnings("null")
    @DisplayName("Test for saving and retrieving a language entity")
    void testSaveAndRetrieveLanguage() {
        // Given
        Language language = Instancio.of(Language.class)
        .set(Select.field(Language::getId), null)
        .set(Select.field(Language::getCode), "hr-HR")
        .create(); // create a new language instance with a null ID
        
        // action: persist the language entity
        testEntityManager.persistAndFlush(language);
        // clear the persistence context to ensure the entity is fetched from the database
        testEntityManager.clear();
        
        // When
        Language found = languageRepository.findByCode(language.getCode()).orElse(null);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isNotNull();
        assertThat(found.getCode()).isEqualTo(language.getCode());
        assertThat(found.getName()).isEqualTo(language.getName());
        assertThat(found.getActive()).isEqualTo(language.getActive());
        assertThat(found.getIsDefault()).isEqualTo(language.getIsDefault());
    }

}
