package com.icms.shared.rules;

import com.icms.shared.entity.IdentifiableImpl;
import com.icms.shared.repository.BaseRepository;
import com.icms.shared.exceptions.BusinessRuleException;
import com.icms.shared.exceptions.EntityNotFoundException;

public class BaseDaoRules<E extends IdentifiableImpl<ID>, R extends BaseRepository<E, ID>, ID> implements DaoRulesImpl<E> {

    final R repository;

    public BaseDaoRules(R repository) {
        this.repository = repository;
    }

    @Override
    public void canSave(E entity) {
        isNew(entity);
    }

    @Override
    public void canUpdate(E entity) {
        hasId(entity);
        existsById(entity.getId());
    }

    @Override
    public void canDelete(E entity) {
        hasId(entity);
        existsById(entity.getId());
    }

    protected void isNew(E entity) {
        if (entity.getId() != null) {
            throw new BusinessRuleException("Ent-004"); 
        }
    }

    protected void hasId(E entity) {
        if (entity.getId() == null) {
            throw new BusinessRuleException("Ent-003"); 
        }
    }

    protected void existsById(ID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Ent-001"); 
        }
    }
}
