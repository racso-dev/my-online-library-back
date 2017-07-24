package com.marketpay.conf.security;

import com.marketpay.filter.CORSFilter;
import com.marketpay.filter.security.JWTAuthenticationFilter;
import com.marketpay.filter.security.JWTLoginFilter;
import com.marketpay.services.auth.TokenAuthenticationService;
import com.marketpay.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

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
            .antMatchers("/").permitAll()
            .antMatchers("/manage/**").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers(HttpMethod.POST, "/login").permitAll()
//            .antMatchers("/api/**").permitAll();
            .antMatchers("/api/**").authenticated()
            .and()
                // We filter the api/login requests
            .addFilterBefore(new JWTLoginFilter("/login", authenticationManager(), tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class)
                    // And filter other requests to check the presence of JWT in header
            .addFilterBefore(new JWTAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new CORSFilter(), JWTLoginFilter.class);
    }

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String userName;
    @Value("${spring.datasource.password}")
    private String password;

    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(driverClassName);
        driverManagerDataSource.setUrl(url);
        driverManagerDataSource.setUsername(userName);
        driverManagerDataSource.setPassword(password);
        return driverManagerDataSource;
    }

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .passwordEncoder(PasswordUtils.PASSWORD_ENCODER)
            .usersByUsernameQuery("select login, password, true from user where login=?")
            .authoritiesByUsernameQuery("select login, profile from user where login=?");
    }

}
