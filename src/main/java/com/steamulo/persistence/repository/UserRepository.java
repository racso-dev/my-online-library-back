package com.steamulo.persistence.repository;

import com.steamulo.persistence.OptionalCRUDRepository;
import com.steamulo.persistence.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Created by antony on 06/07/17.
 */
public interface UserRepository extends OptionalCRUDRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findUserByLoginAndPassword(String login, String password);
    Optional<User> findUserByLogin(String login);
}
