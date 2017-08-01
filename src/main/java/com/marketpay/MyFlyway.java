package com.marketpay;

import com.marketpay.conf.DBConfig;
import com.marketpay.conf.SpringDatasource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by antony on 03/07/17.
 */
public class MyFlyway {
    private static final String DEV_PROFILE = "dev";

    private final Logger logger = LoggerFactory.getLogger(Application.class);

    private static final String DB_INIT = "db/init";
    private static final String DB_DELTA = "db/delta";
    private static final String DB_DEV = "db/dev";

    private final ApplicationContext context;
    private final SpringDatasource springDatasource;
    private DBConfig dbConfig;

    public static void initFlyway(ApplicationContext applicationContext) {
        new MyFlyway(applicationContext);
    }


    /**
     * On vérifie qu'on est bien dans une configuration BDD Flyway, puis on va récupérer les scripts d'init puis les scripts delta
     *
     * @param applicationContext
     */
    private MyFlyway(ApplicationContext applicationContext) {
        this.context = applicationContext;
        this.dbConfig = this.context.getBean(DBConfig.class);
        this.springDatasource = this.context.getBean(SpringDatasource.class);

        if(dbConfig.isFlyway()){
            try {
                safeMigrate(DB_INIT);
                safeMigrate(DB_DELTA);
                Environment environment = context.getEnvironment();
                if(environment != null && environment.getActiveProfiles() != null && Arrays.asList(environment.getActiveProfiles()).contains(DEV_PROFILE)){
                    safeMigrate(DB_DEV);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * On vérifie que tout les éléments sont présents pour commencer la migration Flyway
     *
     * @param folder
     * @throws IOException
     */
    private void safeMigrate(String folder) throws IOException {
        if (context.getClassLoader().getResource(folder) == null) {
            logger.warn("Le dossier renseigné pour Flyway n'existe pas / est vide");
            return;
        }
        if (springDatasource.getUserName() == null) {
            logger.warn("Aucun user configuré pour la migration Flyway");
            return;
        }

        try {
            migrate(folder);
        } catch (Exception f) {
            f.printStackTrace();
            logger.warn("Erreur lors de l'installation du dump par Flyway");
        }
    }


    /**
     * Migration de la base de donnée via Flyway
     *
     * @param folder
     * @throws IOException
     */
    private void migrate(String folder) throws IOException {
        logger.info("Installation MyFlyway - url: " + springDatasource.getUrl() + " - répertoire: " + folder);

        String fullUrl = springDatasource.getUrl();
        String url = fullUrl.substring(0, fullUrl.lastIndexOf("/") + 1);
        String schema = fullUrl.substring(fullUrl.lastIndexOf("/") + 1);
        Flyway flyway = new Flyway();
        flyway.setDataSource(new org.flywaydb.core.internal.util.jdbc.DriverDataSource(Thread.currentThread().getContextClassLoader(),
            springDatasource.getDriverClassName(),
            url,
            springDatasource.getUserName(),
            springDatasource.getPassword()));
        flyway.setSchemas(schema);
        flyway.setBaselineOnMigrate(false);
        flyway.setValidateOnMigrate(false);
        flyway.setOutOfOrder(true);
        flyway.setLocations("filesystem:" + context.getClassLoader().getResource(folder).getPath());
        //si on a des migration donc des installation de dump à faire
        if (flyway.info().pending().length > 0) {
            flyway.migrate();
        } else {
            logger.info("Le dump installé est à jour");
        }
    }
}
