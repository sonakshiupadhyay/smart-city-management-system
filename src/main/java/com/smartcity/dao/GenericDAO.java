package com.smartcity.dao;

import com.smartcity.exception.DatabaseException;
import java.util.List;

/**
 * GenericDAO - Generic DAO interface using Java Generics.
 * Demonstrates Generics concept in Java.
 * Provides a type-safe contract for all DAO implementations.
 * 
 * @param <T> the entity type
 * @author Smart City Management System
 */
public interface GenericDAO<T> {
    
    /**
     * Inserts a new entity into the database.
     * @param entity the entity to insert
     * @return the generated ID
     * @throws DatabaseException if insertion fails
     */
    int insert(T entity) throws DatabaseException;
    
    /**
     * Updates an existing entity in the database.
     * @param entity the entity to update
     * @return true if update was successful
     * @throws DatabaseException if update fails
     */
    boolean update(T entity) throws DatabaseException;
    
    /**
     * Deletes an entity by its ID.
     * @param id the entity ID
     * @return true if deletion was successful
     * @throws DatabaseException if deletion fails
     */
    boolean delete(int id) throws DatabaseException;
    
    /**
     * Finds an entity by its ID.
     * @param id the entity ID
     * @return the found entity or null
     * @throws DatabaseException if query fails
     */
    T findById(int id) throws DatabaseException;
    
    /**
     * Retrieves all entities.
     * @return list of all entities
     * @throws DatabaseException if query fails
     */
    List<T> findAll() throws DatabaseException;
    
    /**
     * Counts total entities.
     * @return total count
     * @throws DatabaseException if query fails
     */
    int count() throws DatabaseException;
}
