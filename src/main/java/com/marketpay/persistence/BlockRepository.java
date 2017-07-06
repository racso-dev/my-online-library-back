package com.marketpay.persistence;

import com.marketpay.persistence.entity.Block;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by antony on 06/07/17.
 */
public interface BlockRepository extends CrudRepository<Block, Long> {
    List<Block> findByFundingDateAndIdBu(LocalDate fundingDate, long idBu);
    List<Block> findByStatus(int status);
}
