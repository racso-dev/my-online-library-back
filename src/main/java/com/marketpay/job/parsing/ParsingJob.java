package com.marketpay.job.parsing;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ParsingJob {

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
    protected int convertStringToInt(String amount) {
        if (amount == null) {
            return -1;
        }
        return Integer.parseInt(amount.trim());
    }

}
