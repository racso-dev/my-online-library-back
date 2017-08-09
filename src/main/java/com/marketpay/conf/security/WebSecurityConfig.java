package com.marketpay.conf.security;

import com.marketpay.filter.security.JWTAuthenticationFilter;
import com.marketpay.filter.security.JWTLoginFilter;
import com.marketpay.services.auth.MarketPayUserDetailsService;
import com.marketpay.services.auth.TokenAuthenticationService;
import com.marketpay.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by sgourio on 10/07/2017.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    TokenAuthenticationService tokenAuthenticationService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .csrf().disable().authorizeRequests()
                .antMatchers("/manage/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/login/**").permitAll()
                .antMatchers("/parsing").permitAll()
                .antMatchers("/swagger**").permitAll()
                .antMatchers("/swagger/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/configuration/**").permitAll()
                .antMatchers("/error").permitAll()
                .anyRequest().authenticated()
            .and()
            // We filter the api/login requests
            .addFilterBefore(new JWTLoginFilter("/login", authenticationManager(), tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class)
            // And filter other requests to check the presence of JWT in header
            .addFilterBefore(new JWTAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    private MarketPayUserDetailsService marketPayUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(marketPayUserDetailsService).passwordEncoder(PasswordUtils.PASSWORD_ENCODER);
    }

}
