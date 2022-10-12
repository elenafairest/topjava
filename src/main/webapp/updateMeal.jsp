<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.temporal.ChronoUnit" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${meal.id != null ? 'Update existing meal' : 'Add new meal'}</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${meal.id != null ? 'Update existing meal' : 'Add new meal'}</h2>
<form method="POST" action='meals' name="frmAddMeal">
    <input type="hidden" readonly="readonly" name="mealId" value="${meal.id}"/> <br/>
    Meal Date : <input
        type="datetime-local" name="dateTime"
        value="${meal.dateTime != null ? meal.dateTime : LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)}"/> <br/>
    Description : <input
        type="text" name="description"
        value="${meal.description}"/> <br/>
    Calories : <input
        type="number" name="calories"
        value="${meal.calories}"/> <br/>
    <input type="submit" value="Submit"/>
    <input type="button" name="cancel" value="Cancel" onclick="window.location.href='meals'"/>
</form>
</body>
</html>