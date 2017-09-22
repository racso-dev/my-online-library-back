package com.steamulo.references;

import java.util.Locale;

/**
 * Created by dle on 22/09/17
 */
public enum LANGUAGE {

    FR(Locale.FRENCH),
    EN(Locale.ENGLISH);

    private Locale locale;

    LANGUAGE(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}
