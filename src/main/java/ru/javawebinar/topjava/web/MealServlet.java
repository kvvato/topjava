package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoList;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class MealServlet extends HttpServlet {
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    private MealDao mealDao;

    public MealServlet() {
        mealDao = new MealDaoList();
        mealDao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        mealDao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        mealDao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        mealDao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        mealDao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        mealDao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        mealDao.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forward = "";
        String action = req.getParameter("action");

        if (action == null) {
            forward = LIST_MEAL;
            req.setAttribute("meals", MealsUtil.filteredByStreams(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX));
        } else if (action.equalsIgnoreCase("add")) {
            forward = INSERT_OR_EDIT;
            req.setAttribute("meal", null);
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int id = Integer.parseInt(req.getParameter("id"));
            Meal meal = mealDao.getById(id);
            req.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            mealDao.delete(id);
            resp.sendRedirect("meals");
            return;
        }

        req.getRequestDispatcher(forward).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        LocalDateTime date = LocalDateTime.parse(req.getParameter("datetime"), formatter);
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        if (action.equalsIgnoreCase("create")) {
            Meal meal = new Meal(date, description, calories);
            mealDao.add(meal);
        } else if (action.equalsIgnoreCase("update")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Meal meal = new Meal(id, date, description, calories);
            mealDao.update(meal);
        }

        resp.sendRedirect("meals");
    }
}
