package com.marketpay.api.user.response;

import com.marketpay.services.user.resource.ShopUserResource;

import java.util.List;

public class ShopUserListResponse {

    private List<ShopUserResource> shopUserResourceList;

    public ShopUserListResponse() {
    }

    public ShopUserListResponse(List<ShopUserResource> shopUserResourceList) {
        this.shopUserResourceList = shopUserResourceList;
    }

    public List<ShopUserResource> getShopUserResourceList() {
        return shopUserResourceList;
    }

    public void setShopUserResourceList(List<ShopUserResource> shopUserResourceList) {
        this.shopUserResourceList = shopUserResourceList;
    }

}
