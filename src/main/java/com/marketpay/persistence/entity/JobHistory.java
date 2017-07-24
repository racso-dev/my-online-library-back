package com.marketpay.persistence.entity;

import com.marketpay.persistence.converter.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by antony on 07/07/17.
 */
@Entity
public class JobHistory {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime date;
    private int status;
    private String filename;
    private String filetype;
    private String jobError;

    public String getJobError() {
        return jobError;
    }

    public void setJobError(String jobError) {
        this.jobError = jobError;
    }

    public void addError(String error) {
        if(this.jobError == null) {
            setJobError(error);
        } else {
            this.jobError = jobError.concat("\\n");
            this.jobError = jobError.concat(error);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }
}
