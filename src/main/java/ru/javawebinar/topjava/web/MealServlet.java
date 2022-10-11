package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoWithCollectionStorage;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String INSERT_OR_EDIT = "/updateMeal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private MealDao dao;

    @Override
    public void init() throws ServletException {
        super.init();
        dao = new MealDaoWithCollectionStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        String action = request.getParameter("action");
        if (action == null) {
            action = "listmeal";
        }
        log.debug("Action is " + action);
        switch (action.toLowerCase()) {
            case "listmeal":
                forward = LIST_MEAL;
                request.setAttribute("mealList", getMealToList());
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
            default:
                forward = INSERT_OR_EDIT;
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    private List<MealTo> getMealToList() {
        return MealsUtil.filteredByStreams(dao.getAll(), null, null, MealsUtil.CALORIES_PER_DAY);
    }

    private static int getMealId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("mealId"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        log.debug("DATE TIME: " + dateTime);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String mealId = request.getParameter("mealId");
        Meal meal = new Meal(dateTime, description, calories);
        if (mealId == null || mealId.isEmpty()) {
            dao.add(meal);
        } else {
            dao.update(getMealId(request), meal);
        }
        request.setAttribute("mealList", getMealToList());
        response.sendRedirect("meals");
    }
}
