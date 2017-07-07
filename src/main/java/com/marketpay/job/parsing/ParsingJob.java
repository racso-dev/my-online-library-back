package com.marketpay.job.parsing;

import com.marketpay.job.parsing.coda.ParsingCODAJob;
import com.marketpay.job.parsing.n43.ParsingN43Job;
import com.marketpay.job.parsing.resources.JobHistory;
import com.marketpay.references.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;

@Component
public abstract class ParsingJob {

    private final Logger LOGGER = LoggerFactory.getLogger(ParsingJob.class);
    private final String CODA_EXTENSION = "BEOC4C.txt";

    @Autowired
    private ParsingCODAJob parsingCoda;

    @Autowired
    private ParsingN43Job parsingN43;

    /**
     * Permet de parser les fichiers en entrée
     * @param filePath
     */
    private void parsingFile(String filePath) {

        //On créé le jobHistory
        JobHistory jobHistory = new JobHistory();
        jobHistory.setStatus(JobStatus.IN_PROGRESS);

        if (filePath == null) {
            LOGGER.error("Le filepath ne peut pas être null");
            saveJobHistory(jobHistory, new Exception("Le filepath ne peut pas être null"));
            return;
        }

        try {
            if (filePath.contains(CODA_EXTENSION)) {
                LOGGER.info("Parsing d'un fichier CODA");
                parsingCoda.parsing(filePath, jobHistory);
            } else {
                LOGGER.info("Parsing d'un fichier N43");
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
     * Fonction générique qui permet de retourner la chaine de caractère matcher par la regex
     * @param line
     * @param regex string a matcher
     * @param indexGroup groupe a récupérer
     * @return l'élément matcher
     */
    protected String matchFromRegex(String line, String regex, int indexGroup) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        if(matcher.find() && matcher.groupCount() >= indexGroup) {
            return matcher.group(indexGroup);
        }
        return null;
    }

    /**
     * Convertie un string en integer
     * @param amount integer
     */
    protected Integer convertStringToInt(String amount) {
        if (amount == null) {
            return -1;
        }

        return Integer.parseInt(amount.trim());
    }

    /**
     * Permet de transformer un string en local date
     * @param format format du string
     * @param dateString string a transformer
     * @return LocalDate
     */
    protected LocalDate convertStringToLocalDate(String format, String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(dateString, formatter);
    }

    /**
     * Method pour parse le fichier
     * @param filePath
     * @param jobHistory
     */
    public abstract void parsing(String filePath, JobHistory jobHistory) throws IOException;

    /**
     * Gestion des erreurs survenue lors du parsing d'un block
     * @param e
     * @param block
     */
    protected abstract void errorBlock(Exception e, List<String> block, JobHistory jobHistory);

    /**
     * Method qui save le jobHistory en fonction du bon déroulement du parsing
     * @param jobHistory
     * @param e
     */
    private void saveJobHistory(JobHistory jobHistory, Exception e){
        if(e == null){
            //Tout est OK
            jobHistory.setStatus(JobStatus.SUCESS);
        } else {
            //Il y a eu une erreur
            jobHistory.setStatus(JobStatus.FAIL);
            jobHistory.addError(e.getMessage());
        }

        //TODO ETI : Save le job history
    }

}
