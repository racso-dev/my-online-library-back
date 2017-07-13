package com.marketpay.persistence.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketpay.persistence.converter.LocalDateAttributeConverter;
import com.marketpay.utils.serializer.LocalDateSerializer;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by antony on 06/07/17.
 */
@Entity
public class Operation {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate fundingDate;
    @Convert(converter = LocalDateAttributeConverter.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate tradeDate;
    private String cardType;
    private int sens;
    private long grossAmount;
    private long netAmount;
    private String contractNumber;
    private String nameShop;
    private long idShop;

    public LocalDate getFundingDate() {
        return fundingDate;
    }

    public void setFundingDate(LocalDate fundingDate) {
        this.fundingDate = fundingDate;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public int getSens() {
        return sens;
    }

    public void setSens(int sens) {
        this.sens = sens;
    }

    public long getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(long grossAmount) {
        this.grossAmount = grossAmount;
    }

    public long getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(long netAmount) {
        this.netAmount = netAmount;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getNameShop() {
        return nameShop;
    }

    public void setNameShop(String nameShop) {
        this.nameShop = nameShop;
    }

    public long getIdShop() {
        return idShop;
    }

    public void setIdShop(long idShop) {
        this.idShop = idShop;
    }
}
