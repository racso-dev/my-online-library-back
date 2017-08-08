package com.marketpay;

import com.marketpay.conf.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by etienne on 03/07/17.
 */
@SpringBootApplication
@EnableScheduling
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) {

        //Lancement de l'appli
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        initFlyway(applicationContext);

        ServerConfig serverConf = applicationContext.getBean(ServerConfig.class);

        LOGGER.info("***************************************************");
        LOGGER.info("* Application MarketPayApi démarrée               *");
        LOGGER.info("* - Port public : " + serverConf.getServer().getPort() + "                            *");
        LOGGER.info("* - Port admin : " + serverConf.getManagement().getPort() + "                             *");
        LOGGER.info("***************************************************");
    }

    private static void initFlyway(ApplicationContext applicationContext) {
        MyFlyway.initFlyway(applicationContext);
    }

}
