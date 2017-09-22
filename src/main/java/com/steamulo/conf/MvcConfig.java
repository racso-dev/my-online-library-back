package com.steamulo.conf;

import com.steamulo.api.ApiInterceptor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    /////////////////////////////////////////////////////////
    //                     INTERCEPTEUR                    //
    /////////////////////////////////////////////////////////
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getApiInterceptor());
        super.addInterceptors(registry);
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
