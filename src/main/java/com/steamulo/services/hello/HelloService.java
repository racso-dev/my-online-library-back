package com.steamulo.services.hello;

import com.steamulo.enums.Language;
import com.steamulo.persistence.entity.User;
import com.steamulo.utils.I18nUtils;
import org.springframework.stereotype.Component;

/**
 * HelloService contient la logique métier qui sera appellé par HelloController
 */
@Component
public class HelloService {

    private final I18nUtils i18nUtils;

    public HelloService(I18nUtils i18nUtils) {
        this.i18nUtils = i18nUtils;
    }

    public String getHelloMessage() {
        return i18nUtils.getMessage("hello", null, Language.FR.getLocale());
    }

    public boolean hasRightToSayHello(User u) {
        return u.getId().equals(1L);
    }
}
