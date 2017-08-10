package com.marketpay.services.mail;

import com.marketpay.references.LANGUAGE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

/**
 * Created by etienne on 09/08/17.
 */
@Component
public class MailBuilder {

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * Construit un MarketPayEmail
     * @param toList
     * @param toHiddenList
     * @param replyTo
     * @param subject
     * @param body
     * @param language
     * @return
     */
    public MarketPayEmail build(List<String> toList, List<String> toHiddenList, String replyTo, String subject, String body, LANGUAGE language) {
        //On construit le mail
        MarketPayEmail email = new MarketPayEmail();
        email.setSubject(subject);
        email.setBody(buildBody(body, language));
        email.setToList(toList);
        email.setHiddenToList(toHiddenList);
        email.setReplyTo(replyTo);

        return email;
    }

    /**
     * Construction du corps du mail avec le template Thymeleaf
     * @return
     */
    public String buildBody(String body, LANGUAGE language){
        Context ctx = new Context();
        ctx.setLocale(language.getLocale());
        ctx.setVariable("content", body);
        return templateEngine.process("email", ctx);
    }

}
