package com.marketpay.references;

/**
 * Created by etienne on 25/07/17.
 */
public enum LOCATION {

    FR("FR"),
    BE("BE"),
    ES("ES");

    private String code;

    LOCATION(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static LOCATION getByCode(String code){
        for(LOCATION location : LOCATION.values()){
            if(location.getCode().equals(code)){
                return location;
            }
        }
        return null;
    }
}
