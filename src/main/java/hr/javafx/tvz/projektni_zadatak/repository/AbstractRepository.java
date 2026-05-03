package hr.javafx.tvz.projektni_zadatak.repository;

import hr.javafx.tvz.projektni_zadatak.enums.Role;
import hr.javafx.tvz.projektni_zadatak.exceptions.RepositoryAccessException;
import hr.javafx.tvz.projektni_zadatak.model.Entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Generic repository interface for basic CRUD operations on entities that extend {@link Entity}.
 * @param <T> the type of entity handled by this repository
 */
public interface AbstractRepository<T extends Entity> {
    List<T> findAll() throws RepositoryAccessException;
    T findById(int id) throws RepositoryAccessException;
    void save(List<T> entities) throws RepositoryAccessException;
    void save(T entity) throws RepositoryAccessException;
    void delete(T entity) throws RepositoryAccessException;
    void update(int id, BigDecimal salary, Role role) throws RepositoryAccessException;
}
