package com.icms.shared.repository;
// import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.Optional;

import com.icms.shared.entity.BaseCatalogTranslationEntity;
import com.icms.shared.entity.Language;
import com.icms.shared.entity.BaseCatalogEntity;

@NoRepositoryBean
public interface BaseCatalogTranslationRepository<E extends BaseCatalogTranslationEntity<C>, C extends BaseCatalogEntity> extends BaseRepository<E, Long> {

    Optional<E> findByCatalogAndLanguage(C catalog, Language language);

    int countByCatalog(C catalog);

}
