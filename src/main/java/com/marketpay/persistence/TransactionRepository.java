package com.marketpay.persistence;

import com.marketpay.persistence.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by antony on 06/07/17.
 */
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findByFundingDateAndIdStore(LocalDate date, long idStore);
}
