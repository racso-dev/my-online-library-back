package com.marketpay.services.mail;

import com.marketpay.conf.EmailConfig;
import com.marketpay.references.LANGUAGE;
import com.marketpay.services.mail.resource.EmailBody;
import com.marketpay.services.mail.resource.EmailSignature;
import com.marketpay.services.mail.resource.MarketPayEmail;
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

    @Autowired
    private EmailConfig emailConfig;

    /**
     * Construit un MarketPayEmail
     * @param toList
     * @param toHiddenList
     * @param subject
     * @param body
     * @param language
     * @return
     */
    public MarketPayEmail build(List<String> toList, List<String> toHiddenList, String subject, EmailBody body, LANGUAGE language) {
        //On construit le mail
        MarketPayEmail email = new MarketPayEmail();
        email.setSubject(subject);
        email.setBody(buildBody(body, language));
        email.setToList(toList);
        email.setHiddenToList(toHiddenList);

        return email;
    }

    /**
     * Construction du corps du mail avec le template Thymeleaf
     * @return
     */
    private String buildBody(EmailBody body, LANGUAGE language){
        //On créé le context mail
        Context ctx = new Context();
        ctx.setLocale(language.getLocale());

        //On ajoute la signature
        ctx.setVariable("signature", getEmailSignature());

        //On ajoute le body
        ctx.setVariable("content", body);

        return templateEngine.process(body.getMailType().getName(), ctx);
    }

    /**
     * Method qui retourne la signature du mail avec les infos de la conf
     * @return
     */
    private EmailSignature getEmailSignature() {
        EmailSignature signature = new EmailSignature();

        signature.setName(emailConfig.getName());
        signature.setAddress(emailConfig.getAddress());
        signature.setPostalCodeAndCity(emailConfig.getPostalCodeAndCity());
        signature.setCountry(emailConfig.getCountry());
        signature.setEmail(emailConfig.getEmail());
        signature.setPhoneNumber(emailConfig.getPhoneNumber());

        return signature;
    }
}
