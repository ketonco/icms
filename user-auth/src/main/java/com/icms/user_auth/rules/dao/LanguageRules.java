package com.icms.user_auth.rules.dao;
import com.icms.shared.rules.BaseDaoCatalogRules;
import com.icms.shared.entity.Language;
import com.icms.shared.exceptions.BusinessRuleException;
import com.icms.user_auth.repository.LanguageRepository;

public class LanguageRules extends BaseDaoCatalogRules<Language, LanguageRepository> {
    private final LanguageRepository languageRepository;

    public LanguageRules(LanguageRepository repository) {
        super(repository);
        this.languageRepository = repository;
    }

    @Override
    public void canSave(Language entity) {
        super.canSave(entity);
        existsByName(entity);
    }

    @Override
    public void canUpdate(Language entity) {
        super.canUpdate(entity);
        existsByName(entity);
        checkIsDefault(entity, "Ent-006");
    }

    @Override
    public void canDelete(Language entity) {
        super.canDelete(entity);
        checkIsDefault(entity, "Ent-005");
    }
    /* Checks if a language with the given name already exists in the repository */
    private void existsByName(Language language) {
        Language existingLanguage = languageRepository.findByName(language.getName());
        // If an existing language with the same name is found and it is not the same as the current language, throw an exception
        if (existingLanguage != null && !existingLanguage.getId().equals(language.getId())) {
            throw new BusinessRuleException("Ent-005"); 
        }
    }

    /* Checks if a language is set as the default language in the repository to not allow deletion or modification */
    private void checkIsDefault(Language language, String code) {
        Language defaultLanguage = languageRepository.findByIsDefault(true);
        if (defaultLanguage != null && defaultLanguage.getId().equals(language.getId())) {
            throw new BusinessRuleException(code);
        }
    }

}
