package com.steamulo.persistence.repository;

import com.steamulo.persistence.OptionalCRUDRepository;
import com.steamulo.persistence.entity.UserToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by etienne on 07/08/17.
 */
public interface UserTokenRepository extends OptionalCRUDRepository<UserToken, Long> {

    Optional<UserToken> findByToken(String token);
    List<UserToken> findByExpirationDateTimeLessThan(LocalDateTime expirationDateTime);

}
