package com.icms.user_auth.repository;
import org.springframework.stereotype.Repository;
import com.icms.shared.repository.BaseCatalogRepository;
import com.icms.shared.entity.Language;

@Repository
public interface LanguageRepository extends BaseCatalogRepository<Language, Long> {

    // Checks if a language with the given name exists in the repository
    boolean existsByName(String name);

    // Finds a language by its name in the repository
    Language findByName(String name);

    // find by isDefault boolean value
    Language findByIsDefault(boolean isDefault);


}
