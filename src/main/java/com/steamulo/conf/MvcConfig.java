package com.steamulo.conf;

import com.steamulo.api.ApiInterceptor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /////////////////////////////////////////////////////////
    //                     INTERCEPTEUR                    //
    /////////////////////////////////////////////////////////
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getApiInterceptor());
    }

    @Bean
    public ApiInterceptor getApiInterceptor() {
        return new ApiInterceptor();
    }

    /////////////////////////////////////////////////////////
    //                         I18n                        //
    /////////////////////////////////////////////////////////
    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource reloadableResourceBundleMessageSource = new ResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasenames(new String[]{"i18n/messages"});
        reloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8");
        reloadableResourceBundleMessageSource.setCacheSeconds(-1);
        return reloadableResourceBundleMessageSource;
    }


}
