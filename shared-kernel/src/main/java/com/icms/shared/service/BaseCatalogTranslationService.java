package com.icms.shared.service;

import com.icms.shared.dto.BaseMapper;
import com.icms.shared.entity.BaseCatalogEntity;
import com.icms.shared.entity.BaseCatalogTranslationEntity;
import com.icms.shared.repository.BaseCatalogTranslationRepository;
import com.icms.shared.rules.BaseDaoCatalogTranslationRules;

public class BaseCatalogTranslationService<E extends BaseCatalogTranslationEntity<C>, D, R extends BaseCatalogTranslationRepository<E, C>, C extends BaseCatalogEntity> 
    extends BaseService<E, D, Long, R> {

    public BaseCatalogTranslationService(
            R repository, 
            BaseMapper<E, D> mapper, 
            BaseDaoCatalogTranslationRules<E, R, C> rules) {
        super(repository, mapper, rules);
    }


}
