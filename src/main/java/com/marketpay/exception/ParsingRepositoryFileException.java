package com.marketpay.exception;

import com.marketpay.job.parsing.repositoryshop.resource.ShopCsvResource;
import com.marketpay.persistence.entity.BusinessUnit;
import com.marketpay.persistence.entity.Shop;

/**
 * Created by etienne on 18/08/17.
 */
public class ParsingRepositoryFileException extends ParsingException {

    private ShopCsvResource shopCsvResource;

    private BusinessUnit businessUnit;

    private Shop shop;

    public ParsingRepositoryFileException(String message, String filePath, String type, ShopCsvResource shopCsvResource, BusinessUnit businessUnit, Shop shop) {
        super(message + toMessage(shopCsvResource, businessUnit, shop), filePath, type);
        this.shopCsvResource = shopCsvResource;
        this.businessUnit = businessUnit;
        this.shop = shop;
    }

    public static String toMessage(ShopCsvResource shopCsvResource, BusinessUnit businessUnit, Shop shop) {
        return " : ( " +
            (shopCsvResource != null ? shopCsvResource.toString() : "") + " , " +
            (businessUnit != null ? businessUnit.toString() : "") + " , " +
            (shop != null ? shop.toString() : "") + " " +
            ") ";
    }

    public ShopCsvResource getShopCsvResource() {
        return shopCsvResource;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public Shop getShop() {
        return shop;
    }
}
