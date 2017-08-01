package com.marketpay.persistence.repository;

import com.marketpay.persistence.OptionalCRUDRepository;
import com.marketpay.persistence.entity.Block;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by antony on 06/07/17.
 */
public interface BlockRepository extends OptionalCRUDRepository<Block, Long> {
    Block findBlockByIdBuAndFundingDate(Long id_bu, LocalDate fundingDate);
}
