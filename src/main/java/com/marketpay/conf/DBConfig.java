package com.marketpay.conf;

import org.springframework.stereotype.Component;

/**
 * Created by etienne on 03/07/17.
 */
@Component
public class DBConfig {
    private boolean flyway;

    public boolean isFlyway() {
        return flyway;
    }

    public void setFlyway(boolean flyway) {
        this.flyway = flyway;
    }

}
