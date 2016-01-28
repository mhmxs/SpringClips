package com.github.springclips.repositories;

import org.springframework.data.repository.Repository;

import java.io.Serializable;

public interface CrudRepository<T, ID extends Serializable> extends Repository<T, ID> {
    <S extends T> S save(S entity);
    T findOne(ID id);
    Iterable<T> findAll();
    void delete(ID id);
}
