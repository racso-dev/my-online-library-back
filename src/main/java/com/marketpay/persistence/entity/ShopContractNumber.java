package com.marketpay.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by etienne on 28/07/17.
 */
@Entity
public class ShopContractNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private long idShop;
    private String contractNumber;

    public Long getId() {
        return id;
    }

    public long getIdShop() {
        return idShop;
    }

    public void setIdShop(long idShop) {
        this.idShop = idShop;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }
}
