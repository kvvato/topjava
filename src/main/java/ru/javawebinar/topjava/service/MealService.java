package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(int userId, Meal meal) {
        return repository.save(userId, meal);
    }

    public void delete(int userId, int id) {
        checkNotFoundWithId(repository.delete(userId, id), id);
    }

    public Meal get(int userId, int id) {
        return checkNotFoundWithId(repository.get(userId, id), id);
    }

    public List<MealTo> getAll(int userId, int calories) {
        return MealsUtil.getTos(repository.getAll(userId), calories);
    }

    public List<MealTo> getAll(int userId, int calories, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        List<Meal> list = repository.getAll(userId, startDate, endDate);
        return MealsUtil.getFilteredTos(list, calories, startTime, endTime);
    }

    public void update(int userId, Meal meal) {
        checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }
}