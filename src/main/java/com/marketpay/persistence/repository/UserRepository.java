package com.marketpay.persistence.repository;

import com.marketpay.persistence.OptionalCRUDRepository;
import com.marketpay.persistence.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Created by antony on 06/07/17.
 */
public interface UserRepository extends OptionalCRUDRepository<User, Long> {
    List<User> findByProfile(int profile);
    Optional<User> findByLogin(String login);
    List<User> findByIdBu(long idBu);
    List<User> findByIdShop(long idShop);
    Optional<User> findUserByLoginAndPassword(String login, String password);
}
