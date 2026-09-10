package com.icms.shared.rules;

public interface DaoRulesImpl<E> {
    void canSave(E entity);
    void canUpdate(E entity);
    void canDelete(E entity);
}
