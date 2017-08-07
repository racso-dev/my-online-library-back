package com.marketpay.services.user.resource;

import java.util.List;

/**
 * Created by kdidelot on 07/08/17.
 */
public class ShopUserListResource {

    private List<ShopUserResource> shopUserList;

    private List<UserResource> superUserList;

    public ShopUserListResource() {
    }

    public List<ShopUserResource> getShopUserList() {
        return shopUserList;
    }

    public void setShopUserList(List<ShopUserResource> shopUserList) {
        this.shopUserList = shopUserList;
    }

    public List<UserResource> getSuperUserList() {
        return superUserList;
    }

    public void setSuperUserList(List<UserResource> superUserList) {
        this.superUserList = superUserList;
    }
}
