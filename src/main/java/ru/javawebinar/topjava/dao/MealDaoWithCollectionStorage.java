package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoWithCollectionStorage implements MealDao {
    private final Map<Integer, Meal> mealCollection = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::add);
    }

    @Override
    public void add(Meal meal) {
        meal.setId(generateId());
        mealCollection.put(meal.getId(), meal);
    }

    @Override
    public void delete(int mealId) {
        mealCollection.remove(mealId);
    }

    @Override
    public void update(int mealId, Meal meal) {
        meal.setId(mealId);
        mealCollection.put(mealId, meal);
    }

    @Override
    public List<Meal> getAll() {
        return new CopyOnWriteArrayList<>(mealCollection.values());
    }

    @Override
    public Meal getById(int mealId) {
        return mealCollection.get(mealId);
    }

    private int generateId() {
        return counter.incrementAndGet();
    }
}
