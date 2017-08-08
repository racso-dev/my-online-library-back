package com.marketpay.services.user.resource;

import com.marketpay.persistence.entity.BusinessUnit;
import com.marketpay.persistence.entity.Shop;

/**
 * Created by etienne on 25/07/17.
 */
public class UserInformationResource {

    private BusinessUnit businessUnit;
    private Shop shop;
    private int profile;
    private String lastName;
    private String firstName;

    public UserInformationResource() {
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
