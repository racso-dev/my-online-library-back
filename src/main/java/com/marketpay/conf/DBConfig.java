package com.marketpay.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by etienne on 03/07/17.
 */
@Component
@ConfigurationProperties(prefix = "database")
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

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setFlyway(boolean flyway) {
        this.flyway = flyway;
    }

}
