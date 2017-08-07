package com.marketpay.api.user.response;

import com.marketpay.services.user.resource.ShopUserListResource;
import com.marketpay.services.user.resource.ShopUserResource;

import java.util.List;

public class ShopUserListResponse {

    private ShopUserListResource shopUserResourceList;

    public ShopUserListResponse(ShopUserListResource shopUserResourceList) {
        this.shopUserResourceList = shopUserResourceList;
    }

    public ShopUserListResource getShopUserResourceList() {
        return shopUserResourceList;
    }

    public void setShopUserResourceList(ShopUserListResource shopUserResourceList) {
        this.shopUserResourceList = shopUserResourceList;
    }
}
