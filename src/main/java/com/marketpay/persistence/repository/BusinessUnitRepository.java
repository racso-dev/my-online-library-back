package com.marketpay.persistence.repository;

import com.marketpay.persistence.OptionalCRUDRepository;
import com.marketpay.persistence.entity.BusinessUnit;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by antony on 06/07/17.
 */
public interface BusinessUnitRepository extends OptionalCRUDRepository<BusinessUnit, Long> {
    //TODO en attente d'un vrai ID de BU
    Optional<BusinessUnit> findFirstByClientId(String clientId);
}
