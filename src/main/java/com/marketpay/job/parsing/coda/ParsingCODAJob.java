package com.marketpay.job.parsing.coda;

import com.marketpay.job.parsing.ParsingJob;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.*;

/**
 * Created by etienne on 03/07/17.
 */
@Component
public class ParsingCODAJob extends ParsingJob {

    private final String ENDBLOCK_REGEX = "^9 .*";
    private final String STORENAME_REGEX = "([a-zA-Z]+ +)+";
    private final String CARDTYPE_REGEX = "[a-zA-Z]+";
    private final String AMOUNT_REGEX = " ([0-1])(\\d{15})"; // groupe 1 : Debit/Crédit, groupe 2 : Montant du Debit/ Credit
    private final String CONTACT_NUMBER_REGEX = "(\\d{7})-";
    private final String GROSS_AMOUNT_REGEX = "(:.[^0-9])([^a-zA-Z ]+)(.*:)"; // groupe 1 : gross amount
    private final String DATE_REGEX = "\\w{3} (\\d+)"; // groupe 1 : date

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
        getCardType(list.get(4) + list.get(5));

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
        System.out.printf("cardType : " + cardType);
        return cardType;
    }

    /**
     * Récupère le type de transaction 0 = Crédit 1 = Débit
     * @param line
     * @return le type de transaction
     */
    private String getSens(String line) {
        String sens = matchFromRegex(line, AMOUNT_REGEX, 1);
        System.out.printf("Sens : " + sens);
        return sens;
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

        if(matcher.find() && matcher.groupCount() > indexGroup) {
            return matcher.group(indexGroup);
        }
        // Rien n'est matcher
        return "";
    }
}
