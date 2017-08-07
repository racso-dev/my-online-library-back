package com.marketpay.persistence.repository;

import com.marketpay.persistence.OptionalCRUDRepository;
import com.marketpay.persistence.entity.Shop;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends OptionalCRUDRepository<Shop, Long>, ShopRepositoryCustom {
    List<Shop> findByIdBu(long idBu);
    Optional<Shop> findByCodeAl(String codeAl);
}
