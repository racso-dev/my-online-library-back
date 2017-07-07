package com.marketpay.persistence;

import com.marketpay.persistence.entity.JobInformation;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by antony on 07/07/17.
 */
public interface JobInformationRepository extends CrudRepository<JobInformation, Long> {
    List<JobInformation> findByStatus(int status);
    List<JobInformation> findByFiletype(String filetype);
    List<JobInformation> findByDateTime(LocalDateTime localDateTime);
    List<JobInformation> findByFilename(String filename);
}
