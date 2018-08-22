package com.steamulo.persistence.repository;

import com.steamulo.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findUserByLoginAndPassword(String login, String password);
}
