package com.steamulo.persistence.repository;

import com.steamulo.persistence.OptionalCRUDRepository;
import com.steamulo.persistence.entity.User;

import java.util.Optional;

public interface UserRepository extends OptionalCRUDRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findUserByLoginAndPassword(String login, String password);
}
