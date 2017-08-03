package com.marketpay.services.user.resource;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Created by etienne on 03/08/17.
 */
public class ShopUserResource {

    @JsonSerialize(using = ToStringSerializer.class)
    private long idShop;
    @JsonSerialize(using = ToStringSerializer.class)
    private long idBu;
    private String name;
    private String codeAl;
//    private List<UserResource>  userList;

}
