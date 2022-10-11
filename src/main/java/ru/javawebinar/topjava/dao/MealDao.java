package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.util.List;

public interface MealDao {
    void addMeal(Meal meal);

    void deleteMeal(int mealId);

    void updateMeal(int mealId, LocalDateTime dateTime, String description, int calories);

    List<MealTo> getAllMeals();

    Meal getMealById(int mealId);

    int generateMealId();
}
