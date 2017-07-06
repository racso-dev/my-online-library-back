package com.marketpay.persistence.entity;

import javax.persistence.Entity;

/**
 * Created by antony on 06/07/17.
 */
@Entity
public class Store {
    private long idBu;
    private String contractNumber;
    private String name;

    public long getIdBu() {
        return idBu;
    }

    public void setIdBu(long idBu) {
        this.idBu = idBu;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
