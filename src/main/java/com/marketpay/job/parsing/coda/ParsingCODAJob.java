package com.marketpay.job.parsing.coda;

import com.marketpay.job.parsing.ParsingJob;
import com.marketpay.references.TransactionSens;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ParsingCODAJob extends ParsingJob {

    /**
     * Permet de découper le fichier en block de n relevés
     * @param filepath : path du fichier à parser
     */
    public void parsingCodaFile(String filepath) {
        try {
            String dailyCoda = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] codas = dailyCoda.split("(?<=\\r\\n9.{127})\\r\\n");
            for (String coda : codas) {
                String[] block = coda.split("\\r\\n");
                parsingCodaBlock(block);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parsingCodaBlock(String[] block) {
        String headerRecipientLine = block[0];
        String headerAccountLine = block[1];
        String footerTotal = block[block.length - 1];

        getBuTitle(headerRecipientLine);
        getCompteNumber(headerAccountLine);
        getTotalAmount(footerTotal);
        for (int i = 4; i < (block.length - 2); i = i + 2) {
            parsingDetailLines(block[i], block[i + 2]);
        }
    }

    public void parsingDetailLines(String detailLine1, String detailLine2) {
        getSens(detailLine1);
        getNetAmount(detailLine1);
        getContractNumber(detailLine1);
        getCardType(detailLine1);
        getGrossAmount(detailLine2);
        getTransactionDate(detailLine1);
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
