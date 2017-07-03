package com.marketpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by etienne on 03/07/17.
 */

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        //Flyway
        //TODO

        //Lancement de l'appli
        SpringApplication.run(Application.class, args);
    }

}
