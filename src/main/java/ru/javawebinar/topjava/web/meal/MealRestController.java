package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
public class MealRestController {
    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        return service.getAll(authUserId(), authUserCaloriesPerDay());
    }

    public List<MealTo> getAll(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        return service.getAll(authUserId(), authUserCaloriesPerDay(), startDate, startTime, endDate, endTime);
    }

    public Meal get(int id) {
        return service.get(authUserId(), id);
    }

    public Meal create(Meal meal) {
        return service.create(authUserId(), meal);
    }

    public void delete(int id) {
        service.delete(authUserId(), id);
    }

    public void update(Meal meal, int id) {
        assureIdConsistent(meal, id);
        service.update(authUserId(), meal);
    }

}