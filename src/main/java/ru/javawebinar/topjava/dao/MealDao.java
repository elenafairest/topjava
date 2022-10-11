package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.util.List;

public interface MealDao {
    void add(Meal meal);

    void delete(int mealId);

    void update(int id, Meal meal);

    List<Meal> getAll();

    Meal getById(int mealId);
}
