package com.marketpay.references;

/**
 * Created by etienne on 25/07/17.
 */
public enum LOCATION {

    FR("FR", null),
    BE("BE", "3"),
    ES("ES", "2");

    private String code;
    private String codeNomenclature;

    LOCATION(String code, String codeNomenclature) {
        this.code = code;
        this.codeNomenclature = codeNomenclature;
    }

    public String getCode() {
        return code;
    }

    public String getCodeNomenclature() {
        return codeNomenclature;
    }

    public static LOCATION getByCode(String code){
        for(LOCATION location : LOCATION.values()){
            if(location.getCode().equals(code)){
                return location;
            }
        }
        return null;
    }

    public static LOCATION getByCodeNomenclature(String codeNomenclature){
        for(LOCATION location : LOCATION.values()){
            if(location.getCodeNomenclature() != null && location.getCodeNomenclature().equals(codeNomenclature)){
                return location;
            }
        }
        return null;
    }
}
