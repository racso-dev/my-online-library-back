package com.marketpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by etienne on 03/07/17.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        //Lancement de l'appli
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        initFlyway(applicationContext);
    }

    private static void initFlyway(ApplicationContext applicationContext) {
        MyFlyway.init(applicationContext);
    }

}
