package com.university.election.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD Repository Interface
 * Demonstrates Generics and Dependency Inversion Principle (DIP)
 *
 * @param <T> Entity type
 * @param <ID> ID type
 */
public interface CrudRepository<T, ID> {
    /**
     * Save an entity
     */
    T save(T entity);

    /**
     * Find entity by ID
     */
    Optional<T> findById(ID id);

    /**
     * Find all entities
     */
    List<T> findAll();

    /**
     * Update an entity
     */
    T update(ID id, T entity);

    /**
     * Delete entity by ID
     */
    void deleteById(ID id);

    /**
     * Check if entity exists by ID
     */
    boolean existsById(ID id);

    /**
     * Count all entities
     */
    long count();
}