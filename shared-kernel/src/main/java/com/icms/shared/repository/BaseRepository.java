package com.icms.shared.repository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.domain.Sort;
import java.util.List;
/*
* BaseRepository is a generic repository interface that provides common CRUD and auditing operations for all entities.
* <T> the type of the entity, e.g., User, Product, etc.
* <ID> the type of the entity's identifier, e.g., Long, UUID, String, etc.
* extends JpaRepository and RevisionRepository
* jpa repository is for basic CRUD operations and sorting
* revision repository is for auditing and versioning of entities
*/
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, RevisionRepository<T, ID, Integer> {

    default List<T> getAllAscending() {
        return findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    default List<T> getAllDescending() {
        return findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
    
}
