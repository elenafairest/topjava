package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final Map<Integer, Set<Integer>> mealsForUser = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        List<Meal> usrMeals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "USR Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "USR Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "USR Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "USR Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "USR Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "USR Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "USR Ужин", 410));
        usrMeals.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            if (!mealsForUser.containsKey(userId)) {
                HashSet<Integer> userMealIds = new HashSet<>();
                userMealIds.add(meal.getId());
                mealsForUser.put(userId, userMealIds);
            }
            else {
                mealsForUser.get(userId).add(meal.getId());
            }
            return meal;
        }
        if (!checkUserId(userId, repository.get(meal.getId()))) {
            return null;
        }
        meal.setUserId(userId);
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (checkUserId(userId, repository.get(id)) && mealsForUser.containsKey(userId)) {
            return repository.remove(id) != null && mealsForUser.get(userId).remove(id);
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        if (checkUserId(userId, meal)) {
            return meal;
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        List<Meal> result = getMealsForUser(userId);
        return result.stream()
                .sorted(Comparator.comparing(Meal::getDateTime, Collections.reverseOrder()))
                .collect(Collectors.toList());
    }

    private List<Meal> getMealsForUser(int userId) {
        List<Meal> result = new ArrayList<>();
        Set<Integer> mealIds = mealsForUser.get(userId);
        if (mealIds != null) {
            mealIds.forEach(id -> {
                Meal meal = repository.get(id);
                if (checkUserId(userId, meal)) {
                    result.add(meal);
                }
            });
        }
        return result;
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return getMealsForUser(userId).stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate))
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .sorted(Comparator.comparing(Meal::getDateTime, Collections.reverseOrder()))
                .collect(Collectors.toList());
    }

    private boolean checkUserId(int userId, Meal meal) {
        return meal != null && meal.getUserId() == userId;
    }
}

