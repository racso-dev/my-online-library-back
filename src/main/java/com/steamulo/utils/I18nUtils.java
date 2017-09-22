package com.steamulo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class I18nUtils {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Retourne un message dans une langue donnée s'il existe, dans la langue par défaut sinon
     * @param key
     * @param params
     * @return
     */
    public String getMessage(String key, String[] params, Locale locale) {
        return applicationContext.getMessage(key, params, locale);
    }
}
