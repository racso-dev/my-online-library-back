package com.steamulo.conf.properties;

import com.steamulo.enums.Environment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Regroupe les configurations se trouvant dans le fichier yml
 */
@Configuration
@ConfigurationProperties
public class EnvProperties {
    private String environment;

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        switch (StringUtils.lowerCase(environment)) {
            case "dev":
            case "development":
                return Environment.DEVELOPMENT;
            case "int":
            case "integration":
                return Environment.INTEGRATION;
            case "rec":
            case "recette":
                return Environment.RECETTE;
            case "preprod":
            case "preproduction":
                return Environment.PREPRODUCTION;
            default:
                return Environment.PRODUCTION;
        }
    }
}
