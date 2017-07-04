package com.marketpay.job.parsing;

import com.marketpay.Application;
import com.marketpay.job.parsing.coda.ParsingCODAJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by etienne on 03/07/17.
 */
@Component
public class ParsingJob {

    private final String CODA_EXTENSION = "BEOC4C.txt";

    private final Logger logger = LoggerFactory.getLogger(Application.class);
    @Autowired
    private ParsingCODAJob parsingCoda;

    private void parsingFile(String filepath) {
        if(filepath.contains(CODA_EXTENSION)) {
            logger.info("Parsing d'un fichier CODA");
            parsingCoda.getBlocksFromCodaFile(filepath);
        } else {
            logger.info("Parsing d'un fichier N43");
        }
    }

}
