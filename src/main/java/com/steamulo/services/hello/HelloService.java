package com.steamulo.services.hello;

import com.steamulo.references.LANGUAGE;
import com.steamulo.utils.I18nUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * HelloService contient la logique métier qui sera appellé par HelloController
 */
@Component
public class HelloService {

    @Autowired
    private I18nUtils i18nUtils;

    public String getHelloMessage() {
        return i18nUtils.getMessage("hello", null, LANGUAGE.FR.getLocale());
    }
}
