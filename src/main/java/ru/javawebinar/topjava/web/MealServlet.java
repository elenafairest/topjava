package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoWithCollection;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String INSERT_OR_EDIT = "/updateMeal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private MealDao dao;

    @Override
    public void init() throws ServletException {
        super.init();
        dao = new MealDaoWithCollection();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        String action = request.getParameter("action");
        if (action == null) {
            action = "listmeal";
        }
        log.debug("Action is {}.", action);
        switch (action.toLowerCase()) {
            case "listmeal":
                forward = LIST_MEAL;
                request.setAttribute("mealList", MealsUtil.filteredByStreams(dao.getAll(), null, null, MealsUtil.CALORIES_PER_DAY));
                break;
            case "delete":
                int mealId = getMealId(request);
                dao.delete(mealId);
                response.sendRedirect("meals");
                return;
            case "edit":
                forward = INSERT_OR_EDIT;
                mealId = getMealId(request);
                Meal meal = dao.getById(mealId);
                request.setAttribute("meal", meal);
                break;
            case "insert":
                forward = INSERT_OR_EDIT;
                break;
            default:
                response.sendRedirect("meals");
                return;
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String mealId = request.getParameter("mealId");
        Meal meal = new Meal(dateTime, description, calories);
        if (mealId == null || mealId.isEmpty()) {
            log.debug("Adding new meal.");
            dao.add(meal);
        } else {
            log.debug("Updating meal with id: :{}", mealId);
            meal.setId(getMealId(request));
            dao.update(meal);
        }
        response.sendRedirect("meals");
    }

    private static int getMealId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("mealId"));
    }
}
