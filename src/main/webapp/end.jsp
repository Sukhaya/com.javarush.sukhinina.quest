<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Квест завершён</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="container mt-5">

<h1>Конец квеста</h1>
<hr>

<c:if test="${isWinning eq true}">
    <p class="text-success fw-bold">Поздравляем, ты победил!</p>
    <div class="text-center my-4">
        <img src="images/win.jpg" class="img-fluid rounded" alt="Бородавочник">
    </div>
</c:if>

<c:if test="${isLosing eq true}">
    <p class="text-danger fw-bold">К сожалению, ты проиграл.</p>
    <div class="text-center my-4">
        <img src="images/lose.jpg" class="img-fluid rounded" alt="Бородавочник">
    </div>
</c:if>

<c:if test="${not empty endText}">
    <p>${endText}</p>
</c:if>

<hr>
<a href="${pageContext.request.contextPath}/start.jsp" class="btn btn-primary">
    Начать заново
</a>


<div class="text-center my-4">
    <img src="images/start.jpg" class="img-fluid rounded" alt="Бородавочник">
</div>

</body>
</html>
