package com.marketpay.job.parsing.coda;

import com.marketpay.job.parsing.ParsingJob;
import com.marketpay.job.parsing.resources.JobHistory;
import com.marketpay.references.JobStatus;
import com.marketpay.references.TransactionSens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ParsingCODAJob extends ParsingJob {

    private final String ENDBLOCK_REGEX = "^9 .*";
    private final Logger LOGGER = LoggerFactory.getLogger(ParsingCODAJob.class);

    /**
     * Permet de découper le fichier en block de n relevés
     * @param filePath : path du fichier à parser
     */
    @Override
    public void parsing(String filePath, JobHistory jobHistory) throws IOException {
        String dailyCoda = new String(Files.readAllBytes(Paths.get(filePath)));
        FileReader input = new FileReader(filePath);
        BufferedReader buffer = new BufferedReader(input);
        List<String> block = new ArrayList<>();
        String line;
        Pattern endBlockPattern = Pattern.compile(ENDBLOCK_REGEX);
        while ((line = buffer.readLine()) != null) {
            block.add(line);
            Matcher matcher = endBlockPattern.matcher(line);
            if (matcher.find()) {
                parsingCodaBlock(block, jobHistory);
                // Ecrasement de la liste
                block.clear();
            }
        }
    }

    @Override
    protected void errorBlock(Exception e, List<String> block, JobHistory jobHistory) {
        //On sauvegarde le block en erreur dans la table block avec un status d'erreur
        //TODO ETI

        // On met à jour le status du job et la liste d'erreur
        jobHistory.setStatus(JobStatus.BLOCK_FAIL);
        String error = jobHistory.getError();
        jobHistory.addError(error);
    }

    /**
     * Permet de parser chacun des blocks coda et enregistre le block en base
     * @param block
     * @param jobHistory
     */
    public void parsingCodaBlock(List<String> block, JobHistory jobHistory) {
        try {
            String headerRecipientLine = block.get(0);
            String headerAccountLine = block.get(1);
            String footerTotal = block.get(block.size() - 1);

            getBuTitle(headerRecipientLine);
            getCompteNumber(headerAccountLine);
            getTotalAmount(footerTotal);
            for (int i = 4; i < (block.size() - 2); i = i + 2) {
                parsingDetailLines(block.get(i), block.get(i + 1));
            }
        } catch (Exception e) {
            LOGGER.error("Une erreur s'est produit pendant le parsing du block CODA", e);
            errorBlock(e, block, jobHistory);
        }
        // TODO: save block
    }

    /**
     * Parse les lignes de transactions et les enregistres en base
     * @param detailLine1
     * @param detailLine2
     */
    public void parsingDetailLines(String detailLine1, String detailLine2) {
        getSens(detailLine1);
        getNetAmount(detailLine1);
        getContractNumber(detailLine1);
        getCardType(detailLine1);
        getGrossAmount(detailLine2);
        getTransactionDate(detailLine1);
        // TODO: Save transaction
    }

    /**
     * Récupération du Bussiness Unit name apartir de la première ligne du block
     * @param firstLine: Première ligne du block
     * @return le  Bussiness Unit name associé au block
     */
    public String getBuTitle(String firstLine) {
        return firstLine.substring(34, 60);
    }

    /**
     * Retourne le numéro du compte
     * @param line
     * @return numéro du compte
     */
    public String getCompteNumber(String line) {
        return line.substring(5, 17);
    }

    /**
     * Récupére le total des transactions
     * @param lastLine
     * @return total des transactions
     */
    public Integer getTotalAmount(String lastLine) {
        String totalAmountString = lastLine.substring(37, 52);
        return convertStringToInt(totalAmountString);
    }

    /**
     * Récupération du type de carte
     * @param line
     * @return le type de carte
     */
    public String getCardType(String line) {
        return line.substring(74, 77);
    }

    /**
     * Récupère le type de transaction 0 = Crédit 1 = Débit
     * @param line
     * @return le type de transaction
     */
    public TransactionSens getSens(String line) {
        Integer sens = convertStringToInt(line.substring(31, 32));
        if(sens == 0) {
            return TransactionSens.CREDIT;
        } else {
            return TransactionSens.DEBIT;
        }
    }

    /**
     * Récupération du net amount
     * @param line
     * @return valeur du net amount
     */
    public Integer getNetAmount(String line) {
        String netAmountString = line.substring(32, 47);
        return convertStringToInt(netAmountString);
    }

    /**
     * Récupération du contract number
     * @param line
     * @return contract number
     */
    public String getContractNumber(String line) {
        return line.substring(62, 69);
    }

    /**
     * Récupération du gross amount
     * @param line
     * @return valeur du gross amount
     */
    public Integer getGrossAmount(String line) {
        String grossAmountString = line.substring(17, 33);
        return convertStringToInt(grossAmountString);
    }

    /**
     * Récupération de la date de transaction
     * @param line
     * @return date de transaction
     */
    public String getTransactionDate(String line) {
        return line.substring(78, 84);
    }
}
