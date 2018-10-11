package com.steamulo.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
     * Retourne une LocalDateTime à partir d'un instant
     * @param instant
     * @return
     */
    public static LocalDateTime getLocalDateTimeFromMillis(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    /**
     * Retourne une LocalDateTime à partir d'une durée millisecondes
     * @param millis
     * @return
     */
    public static LocalDateTime getLocalDateTimeFromInstant(long millis) {
        return getLocalDateTimeFromMillis(Instant.ofEpochMilli(millis));
    }

    /**
     * Convertie une LocalDateTime en Date
     * @param localDateTime
     * @return
     */
    public static Date toDateFromLocalDateTime(LocalDateTime localDateTime) {
        return Date.from(toDateFromInstant(localDateTime));
    }

    /**
     * Convertie une LocalDateTime en Instant
     * @param localDateTime
     * @return
     */
    public static Instant toDateFromInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneOffset.UTC).toInstant();
    }
}
