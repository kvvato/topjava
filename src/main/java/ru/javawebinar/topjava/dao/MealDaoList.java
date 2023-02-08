package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoList implements MealDao {
    private static final List<Meal> list = new CopyOnWriteArrayList<>();
    private static final AtomicInteger counter = new AtomicInteger();

    @Override
    public void add(Meal meal) {
        meal.setId(counter.incrementAndGet());
        list.add(meal);
    }

    @Override
    public void update(Meal meal) {
        Meal old = getById(meal.getId());
        if (old != null) {
            int index = list.indexOf(old);
            list.set(index, meal);
        }
    }

    @Override
    public void delete(int id) {
        Meal meal = getById(id);
        if (meal != null) list.remove(meal);
    }

    @Override
    public Meal getById(int id) {
        for (Meal meal : list) {
            if (meal.getId() == id) return meal;
        }
        return null;
    }

    @Override
    public List<Meal> getAll() {
        return list;
    }
}
