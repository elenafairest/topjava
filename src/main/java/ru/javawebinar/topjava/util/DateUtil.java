package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private DateUtil() {}

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(formatter);
    }
}
