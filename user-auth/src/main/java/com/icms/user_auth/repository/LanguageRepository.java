package com.icms.user_auth.repository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.icms.shared.repository.BaseCatalogRepository;
import com.icms.shared.entity.Language;

@Repository
public interface LanguageRepository extends BaseCatalogRepository<Language, Long> {

    // Checks if a language with the given name exists in the repository
    boolean existsByName(String name);

    // Finds a language by its name in the repository
    Optional<Language> findByName(String name);

    // find by isDefault boolean value
    Optional<Language> findByIsDefault(boolean isDefault);


}
