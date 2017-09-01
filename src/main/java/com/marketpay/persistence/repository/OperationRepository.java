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
    // Récupération des opérations apartir d'une liste d'idshop de la date de financement
    // Les opérations doivent être trier par: contract number, date de transaction, type d'opération et type de carte
    List<Operation> findOperationsByIdShopInAndFundingDateOrderByContractNumberAscTradeDateAscOperationTypeAscCardTypeAsc(List<Long> idShopList, LocalDate date);
    List<Operation> findByContractNumberAndIdShopIsNull(String contractNumber);
    Optional<Operation> findFirstByIdShopInOrderByFundingDateDesc(List<Long> idShopList);
    List<Operation> findByIdJobHistory(long idJobHistory);
    // Récupération des opérations apartir d'une liste d'idshop de la date de financement et la date de transaction ( cas multi financement )
    // Les opérations doivent être trier par: contract number, date de transaction, type d'opération et type de carte
    List<Operation> findOperationsByIdShopInAndFundingDateAndCreateDateOrderByContractNumberAscTradeDateAscOperationTypeAscCardTypeAsc(List<Long> idShopList, LocalDate fundingDate, LocalDate createDate);
}
