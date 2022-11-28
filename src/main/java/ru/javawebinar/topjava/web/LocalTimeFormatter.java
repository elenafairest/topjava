package ru.javawebinar.topjava.web;

import org.springframework.format.Formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

public final class LocalTimeFormatter implements Formatter<LocalTime> {

    public String print(LocalTime lt, Locale locale) {
        return lt.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public LocalTime parse(String formatted, Locale locale) {
        return parseLocalTime(formatted);
    }
}
