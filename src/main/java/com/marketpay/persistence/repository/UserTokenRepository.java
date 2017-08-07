package com.marketpay.persistence.repository;

import com.marketpay.persistence.OptionalCRUDRepository;
import com.marketpay.persistence.entity.UserToken;

import java.util.Optional;

/**
 * Created by etienne on 07/08/17.
 */
public interface UserTokenRepository extends OptionalCRUDRepository<UserToken, Long> {

    Optional<UserToken> findByToken(String token);

}
