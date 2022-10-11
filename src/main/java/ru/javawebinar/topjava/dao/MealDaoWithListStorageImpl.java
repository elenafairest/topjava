package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoWithListStorageImpl implements MealDao {
    private static final List<Meal> mealList = MealsUtil.meals;
    private static final AtomicInteger counter = new AtomicInteger(0);

    static {
        mealList.forEach(meal -> meal.setId(counter.incrementAndGet()));
    }

    @Override
    public void addMeal(Meal meal) {
        mealList.add(meal);
    }

    @Override
    public void deleteMeal(int mealId) {
        mealList.removeIf(mealTo -> mealTo.getId() == mealId);
    }

    @Override
    public void updateMeal(int mealId, LocalDateTime dateTime, String description, int calories) {
        Meal mealById = getMealById(mealId);
        mealById.setDateTime(dateTime);
        mealById.setDescription(description);
        mealById.setCalories(calories);
    }

    @Override
    public List<MealTo> getAllMeals() {
        return MealsUtil.filteredByStreams(MealsUtil.meals, null, null, MealsUtil.CALORIES_PER_DAY);
    }

    @Override
    public Meal getMealById(int mealId) {
        return mealList.stream().filter(meal -> meal.getId() == mealId).findFirst().get();
    }

    @Override
    public int generateMealId() {
        return counter.incrementAndGet();
    }
}
