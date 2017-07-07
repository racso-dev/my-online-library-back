package com.marketpay.persistence;

import com.marketpay.persistence.entity.BusinessUnit;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by antony on 06/07/17.
 */
public interface BusinessUnitRepository extends CrudRepository<BusinessUnit, Long> {
    //TODO en attente d'un vrai ID de BU
    BusinessUnit findFirstByClientId(String clientId);
}
