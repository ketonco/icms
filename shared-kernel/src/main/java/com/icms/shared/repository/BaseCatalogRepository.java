package com.icms.shared.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@NoRepositoryBean 
public interface BaseCatalogRepository<E, ID> extends BaseRepository<E, ID> {

    /* check if an entity with the given code exists */
    public boolean existsByCode(String code);

    /* find an entity by its code */
    @Transactional(readOnly = true)
    public Optional<E> findByCode(String code);

    
    public boolean existsByName(String name);

    // 
    @Transactional(readOnly = true)
    public Optional<E> findByName(String name);

}
