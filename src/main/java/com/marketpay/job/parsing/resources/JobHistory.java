package com.marketpay.job.parsing.resources;

import com.marketpay.references.JobStatus;

import java.util.Date;

public class JobHistory {
    private JobStatus status;
    private Date date;
    private String error;

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void addError(String error) {
        if (this.error != null) {
            this.error.concat("\\n");
            this.error.concat(error);
        } else {
            this.error = error;
        }
    }
}
