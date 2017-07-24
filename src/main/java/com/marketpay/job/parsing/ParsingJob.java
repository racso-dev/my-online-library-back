package com.marketpay.job.parsing;

import com.marketpay.persistence.entity.JobHistory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public abstract class ParsingJob {

    protected final String DATE_FORMAT_FILE = "ddMMyy";


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

}
