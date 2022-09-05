package com.steamulo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@EntityScan(
    basePackageClasses = {Application.class, Jsr310JpaConverters.class}
)
@SpringBootApplication
@EnableCaching
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) {

        //Lancement de l'appli
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        ServerProperties serverConf = applicationContext.getBean(ServerProperties.class);
        ManagementServerProperties managementConf = applicationContext.getBean(ManagementServerProperties.class);

        LOGGER.info("***************************************************");
        LOGGER.info("* Application Basejump démarrée       *");
        LOGGER.info("* - Port public : " + serverConf.getPort() + "                            *");
        LOGGER.info("* - Port admin : " + managementConf.getPort() + "                             *");
        LOGGER.info("***************************************************");
    }
}
