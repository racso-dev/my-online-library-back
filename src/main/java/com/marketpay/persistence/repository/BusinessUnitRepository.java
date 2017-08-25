package com.marketpay.persistence.repository;

import com.marketpay.persistence.OptionalCRUDRepository;
import com.marketpay.persistence.entity.BusinessUnit;

import java.util.Optional;

public interface BusinessUnitRepository extends OptionalCRUDRepository<BusinessUnit, Long> {
    Optional<BusinessUnit> findByCodeBuAndLocation(String codeBu, String location);
}
