package com.marketpay.persistence.repository;

import com.marketpay.persistence.OptionalCRUDRepository;
import com.marketpay.persistence.entity.Operation;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by antony on 06/07/17.
 */
public interface OperationRepository extends OptionalCRUDRepository<Operation, Long> {
    List<Operation> findByFundingDateAndIdShop(LocalDate date, long idShop);
    List<Operation> findOperationsByIdShopInAndFundingDate(List<Long> idShopList, LocalDate date);
    List<Operation> findByContractNumberAndIdShopIsNull(String contractNumber);
}
