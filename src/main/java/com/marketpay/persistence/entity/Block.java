package com.marketpay.persistence.entity;

import com.marketpay.persistence.converter.LocalDateAttributeConverter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by antony on 06/07/17.
 */
@Entity
public class Block {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate fundingDate;
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate createDate;
    @Lob
    private String content;
    private int status;
    private Long idBu;

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getIdBu() {
        return idBu;
    }

    public void setIdBu(Long idBu) {
        this.idBu = idBu;
    }
}
