package com.marketpay.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Created by etienne on 09/08/17.
 */
@Configuration
public class ThymeleafMailConf {

    @Bean
    public ClassLoaderTemplateResolver emailTemplateResolver(){
        ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setTemplateMode("HTML5");
        emailTemplateResolver.setCharacterEncoding("UTF-8");
        emailTemplateResolver.setOrder(1);

        return emailTemplateResolver;
    }

}
