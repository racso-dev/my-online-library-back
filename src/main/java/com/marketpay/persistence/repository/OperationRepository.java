package com.marketpay.persistence.repository;

import com.marketpay.persistence.OptionalCRUDRepository;
import com.marketpay.persistence.entity.Operation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Created by antony on 06/07/17.
 */
public interface OperationRepository extends OptionalCRUDRepository<Operation, Long> {
    List<Operation> findByFundingDateAndIdShop(LocalDate date, long idShop);
    List<Operation> findOperationsByIdShopInAndFundingDate(List<Long> idShopList, LocalDate date);
    List<Operation> findByContractNumberAndIdShopIsNull(String contractNumber);
    Optional<Operation> findFirstByIdShopInOrderByFundingDateDesc(List<Long> idShopList);
    List<Operation> findByCreateDateAndIdShopInAndFundingDate(LocalDate createDate, List<Long> idShopList, LocalDate date);
    List<Operation> findByIdJobHistory(long jobHistory);
}
