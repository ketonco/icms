package com.icms.shared.repository;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean 
public interface BaseCatalogRepository<E, ID> extends BaseRepository<E, ID> {

    /* check if an entity with the given code exists */
    boolean existsByCode(String code);

    /* find an entity by its code */
    E findByCode(String code);

}
