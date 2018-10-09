package com.steamulo.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class I18nUtils {

    private final ApplicationContext applicationContext;

    public I18nUtils(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

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
