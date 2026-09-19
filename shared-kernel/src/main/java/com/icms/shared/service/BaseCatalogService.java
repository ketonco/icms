package com.icms.shared.service;

import org.springframework.transaction.annotation.Transactional;

import com.icms.shared.dto.BaseMapper;
import com.icms.shared.dto.IdentifiableDtoImpl;
import com.icms.shared.entity.BaseCatalogEntity;
import com.icms.shared.repository.BaseCatalogRepository;
import com.icms.shared.rules.BaseDaoCatalogRules;

public class BaseCatalogService<E extends BaseCatalogEntity, D extends IdentifiableDtoImpl<Long>, R extends BaseCatalogRepository<E, Long>> 
    extends BaseService<E, D, Long, R> {

    protected BaseCatalogService(
        R repository, 
        BaseMapper<E, D> mapper, 
        BaseDaoCatalogRules<E, R> rules) {

        super(repository, mapper, rules);
    }

    /* Base catalog service providing common CRUD operations for catalog entities. */

    /* find by code */
    @Transactional(readOnly = true)
    public E findByCode(String code) {
        return repository.findByCode(code).orElse(null);
    }

    /* find dto by code */
    @Transactional(readOnly = true)
    public D findDtoByCode(String code) {
        return mapper.toDto(findByCode(code));
    }

}
