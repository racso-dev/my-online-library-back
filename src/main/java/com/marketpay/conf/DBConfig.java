package com.marketpay.conf;

import org.springframework.context.annotation.Configuration;

/**
 * Created by etienne on 03/07/17.
 */
@Configuration
public class DBConfig {

    private String driver;

    private String url;

    private String user;

    private String password;

    private String packageName;

    private boolean flyway;

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isFlyway() {
        return flyway;
    }
}
