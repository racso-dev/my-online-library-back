package com.marketpay.job.scheduledtask;

import com.marketpay.conf.SFTPConfig;
import com.marketpay.exception.ScheduledTaskException;
import com.marketpay.job.parsing.ParsingDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by etienne on 03/07/17.
 */
@Component
public class ScheduledParsingTask {

    private final Logger LOGGER = LoggerFactory.getLogger(ScheduledParsingTask.class);

    @Autowired
    private SFTPConfig sftpConfig;

    @Autowired
    private ParsingDispatcher parsingDispatcher;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Scheduled(fixedRateString = "${scheduledTask.fixedRate}")
    private void schedulerParsing() throws ScheduledTaskException {
        LOGGER.info("*************** START Scheduled Parsing Task ***************");

        //On récupère le dossier incomming
        File incommingDirectory = new File(sftpConfig.getPathIncomming());
//        if(!incommingDirectory.exists() || !incommingDirectory.isDirectory()){
//            throw new ScheduledTaskException("IncommingDirectory inexistant ou n'est pas un dossier", sftpConfig.getPathIncomming(), sftpConfig.getPathArchive());
//        }

        //On regarde s'il y a des fichiers à parser, On récupère donc la liste des fichiers
        List<File> fileToParseList = Arrays.asList(incommingDirectory.listFiles());

        //On prépare le nouveau directory archive, un directory par jour
        String nameArchiveDirectory = LocalDate.now(). format(formatter);
        File archiveDirectory = new File(sftpConfig.getPathArchive() + File.separator + nameArchiveDirectory);
        //S'il n'existe pas on le crée
        if(!archiveDirectory.exists() && !archiveDirectory.mkdirs()) {
            throw new ScheduledTaskException("Error lors de la création du dossier d'archive", sftpConfig.getPathIncomming(), sftpConfig.getPathArchive());
        }

        //On parse les fichiers
        int nbError = 0;
        int nbSuccess = 0;
        LOGGER.info("*************** " + fileToParseList.size() + " fichiers à parser ***************");
        for(File fileToParse : fileToParseList) {
            try {
                LOGGER.info("*************** Parsing du fichier " + fileToParse + " ***************");
                //On vérifie le fichier
                if (!fileToParse.exists() || !fileToParse.isFile()) {
                    throw new ScheduledTaskException("Fichier " + fileToParse.getAbsolutePath() + " incorrect", sftpConfig.getPathIncomming(), sftpConfig.getPathArchive());
                }

                parsingDispatcher.parsingFile(fileToParse.getAbsolutePath());

                //On déplace le fichier une fois parsé dans archive
                File fileArchive = new File(archiveDirectory + File.separator + fileToParse.getName());
                fileToParse.renameTo(fileArchive);

                nbSuccess++;
            } catch (ScheduledTaskException e) {
                e.printStackTrace();
                nbError++;
            }
        }

        LOGGER.info("*************** SUCCESS : " + nbSuccess + " , ERROR : " + nbError + " ***************");
        LOGGER.info("*************** END Scheduled Parsing Task ***************");
    }


}
