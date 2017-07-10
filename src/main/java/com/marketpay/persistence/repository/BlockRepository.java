package com.marketpay.persistence.repository;

import com.marketpay.persistence.OptionalCRUDRepository;
import com.marketpay.persistence.entity.Block;

import java.util.Optional;

/**
 * Created by antony on 06/07/17.
 */
public interface BlockRepository extends OptionalCRUDRepository<Block, Long> {
    Optional<Block> findFirstById(long id);

}
