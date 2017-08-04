package com.marketpay.job.scheduledsftp;

import com.marketpay.conf.SFTPConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by etienne on 03/07/17.
 */
@Component
public class ScheduledParsingTask {

    private final Logger LOGGER = LoggerFactory.getLogger(ScheduledParsingTask.class);

    @Autowired
    private SFTPConfig sftpConfig;

    @Scheduled(fixedRate = 5000)
    private void schedulerParsing() {

        //On récupère le dossier incomming
        //TODO ETI

        //On regarde s'il y a des fichiers à parser
        //TODO ETI

        //On parse les fichiers
        //TODO ETI

        //On déplace les fichiers une fois parsé dans archive
        //TODO ETI

        //TODO ETI
        System.err.println("Start");
    }


}
