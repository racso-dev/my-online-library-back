package com.marketpay.persistence.repository;

import com.marketpay.persistence.OptionalCRUDRepository;
import com.marketpay.persistence.entity.JobHistory;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by antony on 07/07/17.
 */
public interface JobHistoryRepository extends OptionalCRUDRepository<JobHistory, Long> {
    List<JobHistory> findByStatus(int status);
    List<JobHistory> findByFiletype(String filetype);
    List<JobHistory> findByFilename(String filename);
}
