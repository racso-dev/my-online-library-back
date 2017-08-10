package com.marketpay.utils;

import com.marketpay.references.LANGUAGE;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    /**
     * Retourne une localDateTime à partir d'un instant en millisecondes
     * @param millis
     * @return
     */
    public static LocalDateTime getLocalDateTimeFromInstantMillis(long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    /**
     * Convertie une localDateTime en Date
     * @param localDateTime
     * @return
     */
    public static Date toDateFromLocalDateTime(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Convertie une localDateTime en String dans le format de la langue passée
     * @param localDateTime
     * @param language
     * @return
     */
    public static String toStringLocalDateTime(LocalDateTime localDateTime, LANGUAGE language) {
        return localDateTime.format(DateTimeFormatter.ofPattern(language.getFormatMail(), language.getLocale()));
    }
}
