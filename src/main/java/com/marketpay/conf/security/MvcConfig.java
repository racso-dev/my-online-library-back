package com.marketpay.conf.security;

import com.marketpay.api.MarketPayInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by etienne on 31/07/17.
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getMarketPayInterceptor());
        super.addInterceptors(registry);
    }

    @Bean
    public MarketPayInterceptor getMarketPayInterceptor() {
        return new MarketPayInterceptor();
    }
}
