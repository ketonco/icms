package com.icms.shared.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@NoRepositoryBean 
public interface BaseCatalogRepository<E, ID> extends BaseRepository<E, ID> {

    /* check if an entity with the given code exists */
    boolean existsByCode(String code);

    /* find an entity by its code */
    @Transactional(readOnly = true)
    Optional<E> findByCode(String code);

}
