package com.icms.shared.rules;

import com.icms.shared.entity.BaseCatalogEntity;
import com.icms.shared.repository.BaseCatalogRepository;

public abstract class BaseDaoCatalogRules<E extends BaseCatalogEntity, R extends BaseCatalogRepository<E, Long>>
        extends BaseDaoRules<E, R, Long> {

    public BaseDaoCatalogRules(R repository) {
        super(repository);
    }

    @Override
    public void canSave(E entity) {
        super.canSave(entity);
        validateCodeNotEmpty(entity);
        validateCodeUnique(entity);
    }

    @Override
    public void canUpdate(E entity) {
        super.canUpdate(entity);
        validateCodeNotEmpty(entity);
        validateCodeUnique(entity);
    }

    /* validate if code field is not empty */
    private void validateCodeNotEmpty(E entity) {
        if (entity.getCode() == null || entity.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Code field must not be empty");
        }
    }

    /* validate if code field is unique */
    private void validateCodeUnique(E entity) {
        if (repository.existsByCode(entity.getCode())) {
            throw new IllegalArgumentException("Code field must be unique");
        }
    }

}
