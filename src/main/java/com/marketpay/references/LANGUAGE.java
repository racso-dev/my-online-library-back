package com.marketpay.references;

/**
 * Created by etienne on 03/08/17.
 */
public enum LANGUAGE {

    FR("fr"),
    EN("en"),
    NL("nl"),
    ES("es");

    private String code;

    LANGUAGE(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static LANGUAGE getByCode(String code){
        for(LANGUAGE language: LANGUAGE.values()){
            if(language.getCode().equals(code)){
                return language;
            }
        }
        return null;
    }
}
