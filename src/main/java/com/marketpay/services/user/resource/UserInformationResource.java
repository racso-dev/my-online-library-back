package com.marketpay.services.user.resource;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marketpay.persistence.entity.BusinessUnit;
import com.marketpay.persistence.entity.Shop;
import com.marketpay.utils.serializer.LocalDateSerializer;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by etienne on 25/07/17.
 */
public class UserInformationResource {

    private BusinessUnit businessUnit;
    private List<Shop> shopList;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate lastFundingDate;

    public UserInformationResource(BusinessUnit businessUnit, List<Shop> shopList, LocalDate lastFundingDate) {
        this.businessUnit = businessUnit;
        this.shopList = shopList;
        this.lastFundingDate = lastFundingDate;
    }

    public UserInformationResource() {
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }

    public LocalDate getLastFundingDate() {
        return lastFundingDate;
    }

    public void setLastFundingDate(LocalDate lastFundingDate) {
        this.lastFundingDate = lastFundingDate;
    }
}
