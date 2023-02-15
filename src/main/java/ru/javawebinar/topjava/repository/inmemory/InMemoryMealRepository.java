package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(m -> save(SecurityUtil.authUserId(), m));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        meal.setUserId(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        Meal oldMeal = get(userId, meal.getId());
        if (oldMeal == null) return null;
        return repository.put(meal.getId(), meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        if (get(userId, id) == null) return false;
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId() != userId) return null;
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getFiltered(userIdFilter(userId));
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate) {
        return getFiltered(userIdFilter(userId).and(dateFilter(startDate, endDate)));
    }

    private List<Meal> getFiltered(Predicate<Meal> filter) {
        return repository.values().stream()
                .filter(filter)
                .sorted((m1, m2) -> -m1.getDateTime().compareTo(m2.getDateTime()))
                .collect(Collectors.toList());
    }

    private Predicate<Meal> userIdFilter(int userId) {
        return meal -> meal.getUserId() == userId;
    }

    private <T extends Comparable> Predicate<Meal> dateFilter(T start, T end) {
        return meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), start, end);
    }
}

