package com.marketpay.persistence.repository;

import com.marketpay.persistence.OptionalCRUDRepository;
import com.marketpay.persistence.entity.ShopContractNumber;

import java.util.List;
import java.util.Optional;

/**
 * Created by etienne on 28/07/17.
 */
public interface ShopContractNumberRepository extends OptionalCRUDRepository<ShopContractNumber, Long> {
    List<ShopContractNumber> findByIdShop(long idShop);
    Optional<ShopContractNumber> findByContractNumberAndLocation(String contractNumber, String location);
}
