<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Update existing user/Add new user</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<form method="POST" action='meals' name="frmAddMeal">
    Meal Id : <input
        type="text" readonly="readonly" name="mealId"
        value="<c:out value="${meal.id}" />"/> <br/>
    Meal Date : <input
        type="datetime-local" name="dateTime"
        value="<c:out value="${meal.dateTime}" />"/> <br/>
    Description : <input
        type="text" name="description"
        value="<c:out value="${meal.description}" />"/> <br/>
    Calories : <input
        type="number" name="calories"
        value="<c:out value="${meal.calories}" />"/> <br/>
    <input type="submit" value="Submit"/>
    <input type="button" name="cancel" value="Cancel" onclick="window.location.href='meals?action=listMeal'"/>
</form>
</body>
</html>