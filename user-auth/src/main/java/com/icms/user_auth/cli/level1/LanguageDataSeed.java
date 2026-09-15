package com.icms.user_auth.cli.level1;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.icms.shared.entity.Language;
import com.icms.user_auth.cli.DataSeed;
import com.icms.user_auth.service.daoservice.LanguageService;

@Component 
@Profile("task")
public class LanguageDataSeed implements DataSeed {

    private final LanguageService languageService;

    public LanguageDataSeed(LanguageService languageService) {
        this.languageService = languageService;
    }

    List<Language> languages = List.of(
        new Language("en-US", "English", Boolean.TRUE, Boolean.TRUE),
        new Language("es-ES", "Spanish", Boolean.FALSE, Boolean.TRUE),
        new Language("fr-FR", "French", Boolean.FALSE, Boolean.TRUE)
    );

    @Override
    public int getOrder() {
        return 1; // Set the appropriate order for this seed
    }

    @Override
    public String getName() {
        return "LanguageDataSeed";
    }

    @Override
    public void run() {
        // Implement the seeding logic for languages here
        languages.forEach(this::save);
    }

    public void save(Language language) {
        Language existingLanguage = languageService.findByCode(language.getCode());
        if (existingLanguage != null) {
            // Update the existing language if needed
            existingLanguage.setName(language.getName());
            existingLanguage.setActive(language.getActive());
            existingLanguage.setIsDefault(language.getIsDefault());
            languageService.update(existingLanguage);
        } else {
            languageService.save(language);
        }
    }

}
