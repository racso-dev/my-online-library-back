package com.marketpay.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by etienne on 04/08/17.
 */
@Component
@ConfigurationProperties(prefix = "sftp")
public class SFTPConfig {

    private String pathIncomming;

    private String pathArchive;

    public String getPathIncomming() {
        return pathIncomming;
    }

    public void setPathIncomming(String pathIncomming) {
        this.pathIncomming = pathIncomming;
    }

    public String getPathArchive() {
        return pathArchive;
    }

    public void setPathArchive(String pathArchive) {
        this.pathArchive = pathArchive;
    }
}
