package com.steamulo.utils;

import java.util.regex.Pattern;

public class MailUtils {

    public static final String EMAIL_PATTERN_STRING = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_PATTERN_STRING);

    /**
     * Validation de l'email sans controle DNS
     * @param email Email a tester
     */
    public static boolean checkValidEmail(String email){
        return email!=null && EMAIL_PATTERN.matcher(email).matches() && email.length()<100;
    }

}
