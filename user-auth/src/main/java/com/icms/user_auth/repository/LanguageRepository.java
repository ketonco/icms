package com.icms.user_auth.repository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.icms.shared.repository.BaseCatalogRepository;
import com.icms.shared.entity.Language;

@Repository
public interface LanguageRepository extends BaseCatalogRepository<Language, Long> {

    // find by isDefault boolean value
    Optional<Language> findByIsDefault(boolean isDefault);


}
