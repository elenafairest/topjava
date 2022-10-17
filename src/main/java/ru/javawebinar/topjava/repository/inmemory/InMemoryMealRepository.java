package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(Meal meal, Integer userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        if (!checkUserId(userId, repository.get(meal.getId()))) {
            return null;
        }
        meal.setUserId(userId);
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, Integer userId) {
        Meal mealToDelete = get(id, userId);
        if (mealToDelete == null) {
            return false;
        } else {
            repository.remove(id);
        }
        return true;
    }

    @Override
    public Meal get(int id, Integer userId) {
        Meal meal = repository.get(id);
        if (meal != null && checkUserId(userId, meal)) {
            return meal;
        }
        return null;
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        return repository.values().stream()
                .filter(meal -> checkUserId(userId, meal))
                .sorted(Comparator.comparing(Meal::getDateTime, Collections.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getFiltered(Integer userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return getAll(userId).stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate))
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .collect(Collectors.toList());
    }

    private boolean checkUserId(Integer userId, Meal meal) {
        return meal.isNew() || (meal.getUserId() != null && meal.getUserId().equals(userId));
    }
}

