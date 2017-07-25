package com.marketpay.job.parsing;

import com.marketpay.job.parsing.coda.ParsingCODAJob;
import com.marketpay.job.parsing.n43.ParsingN43Job;
import com.marketpay.job.parsing.repositoryshop.ParsingRepositoryShopJob;
import com.marketpay.persistence.entity.JobHistory;
import com.marketpay.persistence.repository.JobHistoryRepository;
import com.marketpay.references.JOB_STATUS;
import com.marketpay.references.JOB_TYPE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class ParsingDispatcher {

    private final Logger LOGGER = LoggerFactory.getLogger(ParsingJob.class);
    private final String CODA_EXTENSION = "BEOC4C.txt";
    private final String REPOSITORY_SHOP_CSV_EXTENSION = ".csv";

    @Autowired
    private ParsingCODAJob parsingCoda;
    @Autowired
    private ParsingN43Job parsingN43;
    @Autowired
    private ParsingRepositoryShopJob parsingRepositoryShopJob;
    @Autowired
    private JobHistoryRepository jobHistoryRepository;

    /**
     * Permet de parser les fichiers en entrée
     * @param filePath
     */
    public void parsingFile(String filePath) {

        //On créé le jobHistory
        JobHistory jobHistory = new JobHistory();
        jobHistory.setStatus(JOB_STATUS.IN_PROGRESS.getCode());
        jobHistory.setDate(LocalDateTime.now());

        if (filePath == null) {
            LOGGER.error("Le filepath ne peut pas être null");
            saveJobHistory(jobHistory, new Exception("Le filepath ne peut pas être null"));
            return;
        }

        jobHistory.setFilename(filePath);

        try {
            if (filePath.toLowerCase().contains(CODA_EXTENSION.toLowerCase())) {
                LOGGER.info("Parsing d'un fichier CODA");
                jobHistory.setFiletype(JOB_TYPE.CODA.getCode());
                parsingCoda.parsing(filePath, jobHistory);
            } else if(filePath.toLowerCase().contains(REPOSITORY_SHOP_CSV_EXTENSION.toLowerCase())) {
                LOGGER.info("Parsing d'un fichier REFERENTIEL");
                jobHistory.setFiletype(JOB_TYPE.REPOSITORY_SHOP.getCode());
                parsingRepositoryShopJob.parsing(filePath, jobHistory);
            } else {
                LOGGER.info("Parsing d'un fichier N43");
                jobHistory.setFiletype(JOB_TYPE.N43.getCode());
                parsingN43.parsing(filePath, jobHistory);
            }
        } catch (IOException e) {
            LOGGER.error("Une erreur est survenue lors du traitement du fichier " + filePath, e);
            //On met à jour le jobHistory avec l'erreur et on le sauvegarde
            saveJobHistory(jobHistory, e);
            return;
        }

        //Tout c'est bien passé on met à jour JobHistory et on le sauvegarde
        saveJobHistory(jobHistory, null);
    }

    /**
     * Method qui save le jobHistory en fonction du bon déroulement du parsing
     * @param jobHistory
     * @param e
     */
    private void saveJobHistory(JobHistory jobHistory, Exception e){
        if(e == null){
            //Tout est OK
            jobHistory.setStatus(JOB_STATUS.SUCESS.getCode());
        } else {
            //Il y a eu une erreur
            jobHistory.setStatus(JOB_STATUS.FAIL.getCode());
            jobHistory.addError(e.getMessage());
        }
        jobHistoryRepository.save(jobHistory);
    }
}
