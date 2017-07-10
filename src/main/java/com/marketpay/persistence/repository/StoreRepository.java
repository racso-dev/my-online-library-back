package com.marketpay.persistence.repository;

import com.marketpay.persistence.OptionalCRUDRepository;
import com.marketpay.persistence.entity.Store;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by antony on 06/07/17.
 */
public interface StoreRepository extends OptionalCRUDRepository<Store, Long> {
    List<Store> findByIdBu(long idBu);
    List<Store> findByContractNumber(String contractNumber);
    Store findFirstByContractNumber(String contractNumber);
}
