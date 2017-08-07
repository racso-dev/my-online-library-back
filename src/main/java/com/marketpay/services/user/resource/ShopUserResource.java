package com.marketpay.services.user.resource;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.marketpay.persistence.entity.Shop;

import java.util.List;

public class ShopUserResource {

    @JsonSerialize(using = ToStringSerializer.class)
    private long idShop;
    @JsonSerialize(using = ToStringSerializer.class)
    private long idBu;
    private String name;
    private String codeAl;
    private List<UserResource> userList;

    public ShopUserResource(Shop shop, List<UserResource> userResourceList) {
        this.idShop = shop.getId();
        this.idBu = shop.getIdBu();
        this.name = shop.getName();
        this.codeAl = shop.getCodeAl();
        this.userList = userResourceList;
    }

    public long getIdShop() {
        return idShop;
    }

    public void setIdShop(long idShop) {
        this.idShop = idShop;
    }

    public long getIdBu() {
        return idBu;
    }

    public void setIdBu(long idBu) {
        this.idBu = idBu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeAl() {
        return codeAl;
    }

    public void setCodeAl(String codeAl) {
        this.codeAl = codeAl;
    }

    public List<UserResource> getUserList() {
        return userList;
    }

    public void setUserList(List<UserResource> userList) {
        this.userList = userList;
    }

}
