package com.marketpay.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    /**
     * Permet de transformer un string en local date
     * @param format format du string
     * @param dateString string a transformer
     * @return LocalDate
     */
    public static LocalDate convertStringToLocalDate(String format, String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(dateString, formatter);
    }
}
