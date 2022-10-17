package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// TODO add userId
public interface MealRepository {
    // null if updated meal does not belong to userId
    Meal save(Meal meal, Integer userId);

    // false if meal does not belong to userId
    boolean delete(int id, Integer userId);

    // null if meal does not belong to userId
    Meal get(int id, Integer userId);

    // ORDERED dateTime desc
    List<Meal> getAll(Integer userId);

    List<Meal> getFiltered(Integer userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime);
}
