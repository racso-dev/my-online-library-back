package com.marketpay.persistence;

import com.marketpay.persistence.entity.Operation;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by antony on 06/07/17.
 */
public interface OperationRepository extends CrudRepository<Operation, Long> {
    List<Operation> findByFundingDateAndIdStore(LocalDate date, long idStore);
}
