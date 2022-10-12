package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoWithCollection implements MealDao {
    private final Map<Integer, Meal> mealCollection = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::add);
    }

    @Override
    public Meal add(Meal meal) {
        meal.setId(counter.incrementAndGet());
        mealCollection.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public void delete(int id) {
        mealCollection.remove(id);
    }

    @Override
    public Meal update(Meal meal) {
        return mealCollection.computeIfPresent(meal.getId(), (id, m) -> meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealCollection.values());
    }

    @Override
    public Meal getById(int id) {
        return mealCollection.get(id);
    }

}
