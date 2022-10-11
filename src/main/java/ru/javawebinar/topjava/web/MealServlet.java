package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoWithListStorageImpl;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static String INSERT_OR_EDIT = "/updateMeal.jsp";
    private static String LIST_MEAL = "/meals.jsp";
    private MealDao dao;

    public MealServlet() {
        super();
        this.dao = new MealDaoWithListStorageImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        String action = request.getParameter("action");
        if ("listMeal".equals(action)) {
            forward = LIST_MEAL;
            request.setAttribute("mealList", dao.getAllMeals());
        } else if ("delete".equals(action)) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            dao.deleteMeal(mealId);
            request.setAttribute("mealList", dao.getAllMeals());
            response.sendRedirect("meals?action=listMeal");
            return;
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = dao.getMealById(mealId);
            request.setAttribute("meal", meal);
        } else {
            forward = INSERT_OR_EDIT;
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LocalDateTime dateTime = LocalDateTime.from(formatter.parse(request.getParameter("dateTime").replace("T", " ")));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            Meal meal = new Meal(dateTime, description, calories);
            meal.setId(dao.generateMealId());
            dao.addMeal(meal);
        } else {
            dao.updateMeal(Integer.parseInt(mealId), dateTime, description, calories);
        }
        request.setAttribute("mealList", dao.getAllMeals());
        request.getRequestDispatcher(LIST_MEAL).forward(request, response);
    }
}
