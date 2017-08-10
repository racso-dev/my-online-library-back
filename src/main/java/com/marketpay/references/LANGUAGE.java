package com.marketpay.references;

import java.util.Locale;

/**
 * Created by etienne on 03/08/17.
 */
public enum LANGUAGE {

    FR("fr", Locale.FRENCH, "EE dd/MM/yyyy 'à' HH'h'mm"),
    EN("en", Locale.ENGLISH, "EE MM/dd/yyyy HH:mm"),
    NL("nl", new Locale("nl", "BE"), "EE dd/MM/yyyy 'om' HH'h'mm"),
    ES("es", new Locale("es", "ES"), "EE dd/MM/yyyy 'a las' HH'h'mm");

    private String code;
    private Locale locale;
    private String formatMail;

    LANGUAGE(String code, Locale locale, String formatMail) {
        this.code = code;
        this.locale = locale;
        this.formatMail = formatMail;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getCode() {
        return code;
    }

    public String getFormatMail() {
        return formatMail;
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
