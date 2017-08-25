package com.marketpay.persistence.repository;

import com.marketpay.persistence.OptionalCRUDRepository;
import com.marketpay.persistence.entity.Shop;

import java.util.List;
import java.util.Optional;

/**
 * Created by antony on 06/07/17.
 */
public interface ShopRepository extends OptionalCRUDRepository<Shop, Long>, ShopRepositoryCustom {
    List<Shop> findByIdBu(long idBu);
    Optional<Shop> findByCodeAlAndLocation(String codeAl, String location);
}
