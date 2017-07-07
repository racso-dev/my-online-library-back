package com.marketpay.persistence;

import com.marketpay.persistence.entity.JobHistory;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by antony on 07/07/17.
 */
public interface JobHistoryRepository extends CrudRepository<JobHistory, Long> {
    List<JobHistory> findByStatus(int status);
    List<JobHistory> findByFiletype(String filetype);
    List<JobHistory> findByDateTime(LocalDateTime localDateTime);
    List<JobHistory> findByFilename(String filename);
}
