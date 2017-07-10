package com.marketpay.persistence;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by tchekroun on 10/07/2017.
 */
@NoRepositoryBean
public interface OptionalCRUDRepository<T, ID extends Serializable> extends Repository<T, ID> {

    <S extends T> S save(S var1);

    <S extends T> Iterable<S> save(Iterable<S> var1);

    Optional<T> findOne(ID var1);

    boolean exists(ID var1);

    Iterable<T> findAll();

    Iterable<T> findAll(Iterable<ID> var1);

    long count();

    void delete(ID var1);

    void delete(T var1);

    void delete(Iterable<? extends T> var1);

    void deleteAll();
}
