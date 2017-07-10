package com.marketpay.persistence.entity;

import com.marketpay.persistence.converter.LocalDateAttributeConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by antony on 06/07/17.
 */
@Entity
public class Block {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDateTime fundingDate;
    @Lob
    private String content;
    private int status;
    private long idBu;

    public LocalDateTime getFundingDate() {
        return fundingDate;
    }

    public void setFundingDate(LocalDateTime fundingDate) {
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
