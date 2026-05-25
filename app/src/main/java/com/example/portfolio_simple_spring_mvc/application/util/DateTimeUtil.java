package com.example.portfolio_simple_spring_mvc.application.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {

    private static final DateTimeFormatter DATETIME_TO_STRING =
        DateTimeFormatter.ofPattern("yyyy/MM/dd/ HH:mm:ss");

    private static final DateTimeFormatter DATE_TO_STRING =
        DateTimeFormatter.ofPattern("yyyy/MM/dd");

    //インスタンス化禁止
    private DateTimeUtil() {}

    //LocalDateTime -> String
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;

        return dateTime.format(DATETIME_TO_STRING);
    }

    //LocalDate -> String
    public static String formatDate(LocalDate date) {
        if (date == null) return null;

        return date.format(DATE_TO_STRING);
    }

    //String -> LocalDateTime
    public static LocalDateTime parseDateTime(String str) {
        if (str == null || str.isBlank()) return null;

        return LocalDateTime.parse(str, DATETIME_TO_STRING);
    }

    //String -> LocalDate
    public static LocalDate parseDate(String str) {
        if (str == null || str.isBlank()) return null;

        return LocalDate.parse(str, DATE_TO_STRING);
    }
}