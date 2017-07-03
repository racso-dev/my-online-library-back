package com.marketpay;

import com.marketpay.conf.DBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Created by antony on 03/07/17.
 */
public class Flyway {

    private final ApplicationContext context;
    @Autowired
    private DBConfig dbConfig;

    public static void init(ApplicationContext applicationContext) {
        new Flyway(applicationContext);
    }

    public Flyway(ApplicationContext applicationContext) {
        this.context = applicationContext;
    }
}
