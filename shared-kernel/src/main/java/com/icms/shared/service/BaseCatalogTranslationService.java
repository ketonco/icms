package com.icms.shared.service;

import com.icms.shared.dto.BaseMapper;
import java.util.List;
import com.icms.shared.entity.BaseCatalogEntity;
import com.icms.shared.entity.BaseCatalogTranslationEntity;
import com.icms.shared.entity.Language;
import com.icms.shared.repository.BaseCatalogTranslationRepository;
import com.icms.shared.rules.BaseDaoCatalogTranslationRules;

public class BaseCatalogTranslationService<E extends BaseCatalogTranslationEntity<C>, D, R extends BaseCatalogTranslationRepository<E, C>, C extends BaseCatalogEntity> 
    extends BaseService<E, D, Long, R> {

    protected BaseCatalogTranslationRepository<E, C> repository;

    protected BaseMapper<E, D> mapper;

    public BaseCatalogTranslationService(
            R repository, 
            BaseMapper<E, D> mapper, 
            BaseDaoCatalogTranslationRules<E, R, C> rules) {
        super(repository, mapper, rules);
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<E> findByCatalog(C catalog) {
        return repository.findByCatalog(catalog);
    }

    public List<D> findByCatalogDto(C catalog) {
        List<E> entities = repository.findByCatalog(catalog);
        return entities.stream().map(mapper::toDto).toList();
    }

    

    public E findByCatalogAndLanguage(C catalog, Language language) {
        return repository.findByCatalogAndLanguage(catalog, language).orElse(null);
    }

    public D findByCatalogAndLanguageDto(C catalog, Language language) {
        return mapper.toDto(findByCatalogAndLanguage(catalog, language));
    }
}
