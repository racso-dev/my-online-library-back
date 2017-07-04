package com.marketpay.job.parsing.coda;

import com.marketpay.job.parsing.ParsingJob;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.*;

@Component
public class ParsingCODAJob extends ParsingJob {

    private final String ENDBLOCK_REGEX = "^9 .*";
    private final String STORENAME_REGEX = "([a-zA-Z]+ +)+";
    private final String CARDTYPE_REGEX = "[a-zA-Z]+";
    private final String AMOUNT_REGEX = " ([0-1])(\\d{15})"; // groupe 1 : Debit/Crédit, groupe 2 : Montant du Debit/ Credit
    private final String CONTRACT_NUMBER_REGEX = "(\\d{7})-"; // groupe 1 : Contract number
    private final String GROSS_AMOUNT_REGEX = "(:.[^0-9])([^a-zA-Z ]+)(.*:)"; // groupe 1 : gross amount
    private final String DATE_REGEX = "\\w{3} (\\d+)"; // groupe 1 : date
    private final String CREDIT_LINE_REGEX = "^\\d{29}  \\d{38}-\\d{3} \\w{2,3} +\\d{6}"; // Regex pour repérer les lignes de crédit

    /**
     * Permet de découper le fichier en block de n relevés
     * @param filepath : path du fichier à parser
     */
    public void getBlocksFromCodaFile(String filepath) {
        try {
            FileReader input = new FileReader(filepath);
            BufferedReader buffer = new BufferedReader(input);
            ArrayList<String> myList = new ArrayList<>();
            String line;
            Pattern endBlockPattern = Pattern.compile(ENDBLOCK_REGEX);
            while ((line = buffer.readLine()) != null) {
                myList.add(line);
                Matcher matcher = endBlockPattern.matcher(line);
                if (matcher.find()) {
                    traitementDuBlock(myList);
                    // Ecrasement de la liste
                    myList.clear();
                }
            }

            // Fermeture du fichier
            buffer.close();
            input.close();


        } catch (FileNotFoundException e) {
            // TODO: Log file not found
            e.printStackTrace();

        } catch (IOException e) {
            // TODO: Log can't close file
            e.printStackTrace();
        }
    }

    /**
     * Récupère les informations du blocks et les enregistres en bases
     * @param list
     */
    private void traitementDuBlock(ArrayList<String> list) {
        if (list.isEmpty()) {
            return;
        }

        getStoreName(list.get(0));
        for(int i = 0; i < list.size(); i++) {
            String test = matchFromRegex(list.get(i), CREDIT_LINE_REGEX, 0);
            if(!matchFromRegex(list.get(i), CREDIT_LINE_REGEX, 0).isEmpty()) {
                String line = list.get(i);
                // Lancement du parsing de la ligne et récupération de la ligne n + 1
                getCardType(line);
                getSens(line);
                getNetAmount(line);
                getGrossAmount(list.get(i+1));
                getContractNumber(line);
                getTransactionDate(line);
            }
        }

        System.out.println(list.size());
    }

    /**
     * Récupération du store name apartir de la première ligne du block
     * @param firstLine: Première ligne du block
     * @return le store name associé au block
     */
    private String getStoreName(String firstLine) {
        String storeName = matchFromRegex(firstLine, STORENAME_REGEX, 0);
        System.out.println("Store Name : " + storeName);
        return storeName;
    }

    /**
     * Récupération du type de carte
     * @param line
     * @return le type de carte
     */
    private String getCardType(String line) {
        String cardType = matchFromRegex(line, CARDTYPE_REGEX, 0);
        System.out.println("cardType : " + cardType);
        return cardType;
    }

    /**
     * Récupère le type de transaction 0 = Crédit 1 = Débit
     * @param line
     * @return le type de transaction
     */
    private String getSens(String line) {
        String sens = matchFromRegex(line, AMOUNT_REGEX, 1);
        System.out.println("Sens : " + sens);
        return sens;
    }

    /**
     * Récupération du net amount
     * @param line
     * @return valeur du net amount
     */
    private String getNetAmount(String line) {
        String netAmount = matchFromRegex(line, AMOUNT_REGEX, 2);
        System.out.println("netAmount = " + netAmount);
        return netAmount;
    }

    /**
     * Récupération du contract number
     * @param line
     * @return contract number
     */
    private String getContractNumber(String line) {
        String contactNumber = matchFromRegex(line, CONTRACT_NUMBER_REGEX, 1);
        System.out.println("contactNumber = " + contactNumber);
        return contactNumber;
    }

    /**
     * Récupération du gross amount
     * @param line
     * @return valeur du gross amount
     */
    private String getGrossAmount(String line) {
        String grossAmount = matchFromRegex(line, GROSS_AMOUNT_REGEX, 1);
        System.out.println("grossAmount = " + grossAmount);
        return grossAmount;
    }

    /**
     * Récupération de la date de transaction
     * @param line
     * @return date de transaction
     */
    private String getTransactionDate(String line) {
        String date = matchFromRegex(line, DATE_REGEX, 1);
        System.out.println("date = " + date);
        return date;
    }

    /**
     * Fonction générique qui permet de retourner la chaine de caractère matcher par la regex
     * @param line
     * @param regex string a matcher
     * @param indexGroup groupe a récupérer
     * @return l'élément matcher
     */
    private String matchFromRegex(String line, String regex, int indexGroup) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        if(matcher.find() && matcher.groupCount() >= indexGroup) {
            return matcher.group(indexGroup);
        }
        // Rien n'est matcher
        return "";
    }
}
