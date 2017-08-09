package com.marketpay.persistence.repository;

import com.marketpay.persistence.OptionalCRUDRepository;
import com.marketpay.persistence.entity.UserKeyPass;

import java.util.Optional;

/**
 * Created by etienne on 09/08/17.
 */
public interface UserKeyPassRepository extends OptionalCRUDRepository<UserKeyPass, Long> {

    Optional<UserKeyPass> findByIdUser(long idUser);
    Optional<UserKeyPass> findByKeyPass(String keyPass);

}
