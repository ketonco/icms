package com.icms.shared.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.icms.shared.dto.BaseMapper;
import com.icms.shared.entity.AuditableEntity;
import com.icms.shared.repository.BaseRepository;
import com.icms.shared.rules.BaseDaoRules;

public abstract class BaseService<E extends AuditableEntity<ID>,D, ID, R extends BaseRepository<E, ID>> {

    protected final R repository;
    protected final BaseMapper<E, D> mapper;
    protected final BaseDaoRules<E, R, ID> rules;

    protected BaseService(
        R repository, 
        BaseMapper<E, D> mapper, 
        BaseDaoRules<E, R, ID> rules) {
            
        this.repository = repository;
        this.mapper = mapper;
        this.rules = rules;
    }

    /* Base service providing common CRUD operations for entities. */

    /* find all entities */
    @Transactional(readOnly = true)
    public Iterable<E> findAll() {
        return repository.findAll();
    }

    /* find all dto */
    @Transactional(readOnly = true)
    public List<D> findAllDto() {
        return mapper.toDtoList(repository.findAll());
    }

    /* find all entities in descending order */
    @Transactional(readOnly = true)
    public Iterable<E> findAllDescending() {
        return repository.getAllDescending();
    }

    /* find all dto in descending order */
    @Transactional(readOnly = true)
    public List<D> findAllDescendingDto() {
        return mapper.toDtoList(repository.getAllDescending());
    }

    /* find all entities in ascending order */
    @Transactional(readOnly = true)
    public Iterable<E> findAllAscending() {
        return repository.getAllAscending();
    }

    /* find all dto in ascending order */
    @Transactional(readOnly = true)
    public List<D> findAllAscendingDto() {
        return mapper.toDtoList(repository.getAllAscending());
    }

    /* find entity by id */
    @Transactional(readOnly = true)
    public E findById(ID id) {
        return repository.findById(id).orElse(null);
    }

    /* find dto by id */
    @Transactional(readOnly = true)
    public D findDtoById(ID id) {
        return mapper.toDto(repository.findById(id).orElse(null));
    }

    /* check if entity exists by id */
    @Transactional(readOnly = true)
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    /* save entity */
    @Transactional
    public E save(E entity) {
        rules.canSave(entity);
        return repository.save(entity);
    }

    /* save dto */
    @Transactional
    public D saveDto(D dto) {
        E entity = mapper.toEntity(dto);
        entity = save(entity);
        return mapper.toDto(entity);
    }

    /* update entity */
    @Transactional
    public E update(E entity) {
        rules.canUpdate(entity);
        return repository.save(entity);
    }

    /* update dto */
    @Transactional
    public D updateDto(D dto) {
        E entity = mapper.toEntity(dto);
        entity = update(entity);
        return mapper.toDto(entity);
    }

    /* delete entity by id */
    @Transactional
    public void deleteById(ID id) {
        E entity = findById(id);
        rules.canDelete(entity);
        repository.deleteById(id);
    }
}
