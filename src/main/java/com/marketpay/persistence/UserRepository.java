package com.marketpay.persistence;

import com.marketpay.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by antony on 06/07/17.
 */
public interface UserRepository extends CrudRepository<User, Long>{
    List<User> findByProfile(int profile);
    List<User> findByEmail(String email);
    List<User> findByLogin(String login);
    List<User> findByIdBu(long idBu);
    List<User> findByIdStore(String idStore);
}
