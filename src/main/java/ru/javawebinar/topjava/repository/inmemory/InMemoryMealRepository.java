package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        MealsUtil.usrMeals.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> userMeals = repository.computeIfAbsent(userId, ConcurrentHashMap::new);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            userMeals.put(meal.getId(), meal);
            return meal;
        }
        if (!checkUserId(userId, meal.getId())) {
            return null;
        }
        meal.setUserId(userId);
        return userMeals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return checkUserId(userId, id) && repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        if (checkUserId(userId, id)) {
            return repository.get(userId).get(id);
        }
        return null;
    }

    private boolean checkUserId(int userId, int mealId) {
        return repository.containsKey(userId) && repository.get(userId).containsKey(mealId)
                && repository.get(userId).get(mealId) != null
                && repository.get(userId).get(mealId).getUserId() == userId;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return filterByPredicate(userId, meal -> true);
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        return filterByPredicate(userId, meal -> DateTimeUtil.isBetweenClosed(meal.getDate(), startDate, endDate));
    }

    private List<Meal> filterByPredicate(int userId, Predicate<Meal> filterByDate) {
        return getForUser(userId).stream()
                .filter(filterByDate)
                .sorted(Comparator.comparing(Meal::getDateTime, Collections.reverseOrder()))
                .collect(Collectors.toList());
    }

    private Collection<Meal> getForUser(int userId) {
        return repository.containsKey(userId) ? repository.get(userId).values() : Collections.emptyList();
    }
}

