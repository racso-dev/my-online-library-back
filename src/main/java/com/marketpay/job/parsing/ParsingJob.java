package com.marketpay.job.parsing;

import com.marketpay.Application;
import com.marketpay.job.parsing.coda.ParsingCODAJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParsingJob {

    private final String CODA_EXTENSION = "BEOC4C.txt";

    private final Logger logger = LoggerFactory.getLogger(Application.class);
    @Autowired
    private ParsingCODAJob parsingCoda;

    /**
     * Permet de parser les fichiers en entrée
     * @param filepath path du fichier
     */
    private void parsingFile(String filepath) {
        if (filepath == null) {
            logger.warn("Le filepath ne peut pas être null");
            return;
        }
        if(filepath.contains(CODA_EXTENSION)) {
            logger.info("Parsing d'un fichier CODA");
            parsingCoda.parsingCodaFile(filepath);
        } else {
            logger.info("Parsing d'un fichier N43");
            // TODO: ajout du parsing du fichier N43
        }
    }

}
