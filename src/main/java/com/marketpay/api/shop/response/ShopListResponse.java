package com.marketpay.api.shop.response;

import com.marketpay.persistence.entity.Shop;

import java.util.List;

/**
 * Created by etienne on 07/08/17.
 */
public class ShopListResponse {

    private List<Shop> shopList;

    public ShopListResponse(List<Shop> shopList) {
        this.shopList = shopList;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
