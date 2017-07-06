package com.marketpay.persistence.entity;

import javax.persistence.Entity;
import java.time.LocalDate;

/**
 * Created by antony on 06/07/17.
 */
@Entity
public class Block {
    private LocalDate fundingDate;
    private String content;
    private int status;
    private long idBu;

    public LocalDate getFundingDate() {
        return fundingDate;
    }

    public void setFundingDate(LocalDate fundingDate) {
        this.fundingDate = fundingDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getIdBu() {
        return idBu;
    }

    public void setIdBu(long idBu) {
        this.idBu = idBu;
    }
}
