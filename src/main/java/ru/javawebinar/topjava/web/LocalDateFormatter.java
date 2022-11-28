package ru.javawebinar.topjava.web;

import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;

public class LocalDateFormatter implements Formatter<LocalDate> {

    public String print(LocalDate ld, Locale locale) {
        return ld.format(DateTimeFormatter.ISO_DATE);
    }

    public LocalDate parse(String formatted, Locale locale) {
        return parseLocalDate(formatted);
    }
}
