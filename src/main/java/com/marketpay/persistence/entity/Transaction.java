package com.marketpay.persistence.entity;

import javax.persistence.Entity;
import java.time.LocalDate;

/**
 * Created by antony on 06/07/17.
 */
@Entity
public class Transaction {
    private LocalDate fundingDate;
    private LocalDate tradeDate;
    private String cardType;
    private int sens;
    private long gross_amount;
    private long net_amount;
    private String contractNumber;
    private String nameStore;
    private long idStore;

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

    public long getGross_amount() {
        return gross_amount;
    }

    public void setGross_amount(long gross_amount) {
        this.gross_amount = gross_amount;
    }

    public long getNet_amount() {
        return net_amount;
    }

    public void setNet_amount(long net_amount) {
        this.net_amount = net_amount;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }

    public long getIdStore() {
        return idStore;
    }

    public void setIdStore(long idStore) {
        this.idStore = idStore;
    }
}
