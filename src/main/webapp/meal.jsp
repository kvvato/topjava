<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${ meal == null ? 'Add meal' : 'Edit meal' }</h2>
<form action="meals${meal == null ? '?action=create' : '?action=update&id=' += meal.id}" method="post">
    <p>Date: <input type="datetime-local" name="datetime" value="${meal == null ? '' : meal.dateTime}"></p>
    <p>Description: <input type="text" name="description" value="${meal == null ? '' : meal.description}"></p>
    <p>Calories: <input type="text" name="calories" value="${meal == null ? '' : meal.calories}"></p>
    <p>
        <button type="submit">Save</button>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </p>
</form>
</body>
</html>