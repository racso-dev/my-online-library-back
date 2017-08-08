package com.marketpay.references;

import java.util.Locale;

/**
 * Created by etienne on 03/08/17.
 */
public enum LANGUAGE {

    FR("fr", Locale.FRENCH),
    EN("en", Locale.ENGLISH),
    NL("nl", new Locale("nl", "BE")),
    ES("es", new Locale("es", "ES"));

    private String code;
    private Locale locale;

    LANGUAGE(String code, Locale locale) {
        this.code = code;
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
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
