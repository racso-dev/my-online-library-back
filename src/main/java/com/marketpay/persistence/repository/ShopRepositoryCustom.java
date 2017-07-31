package com.marketpay.persistence.repository;

import com.marketpay.persistence.entity.Shop;

import java.util.Optional;

/**
 * Created by etienne on 28/07/17.
 */
public interface ShopRepositoryCustom {

    /**
     * Methode repository qui recherche un shop par contractNumber via la table shopContractNumber
     * @param contractNumber
     * @return
     */
    Optional<Shop> findByContractNumber(String contractNumber);

}
