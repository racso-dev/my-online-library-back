package com.marketpay.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by etienne on 08/08/17.
 */
@Component
@ConfigurationProperties(prefix = "email")
public class EmailConfig {

    private String filter;

    private String emailFrom;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }
}
