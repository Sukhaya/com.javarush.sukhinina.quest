<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String userName = (String) session.getAttribute("userName");
%>
<head>
    <title>World of Warcraft Quest</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="container mt-5">

<h2>Привет, <%= userName %>!</h2>

<h3>${question.text}</h3>

<form method="post" action="${pageContext.request.contextPath}/quest">
    <div class="d-flex flex-wrap gap-2">
        <c:forEach var="answer" items="${question.answers}">
            <a href="${pageContext.request.contextPath}/quest?questionId=${answer.nextQuestionId}"
               title="${pageContext.request.contextPath}/quest?questionId=${answer.nextQuestionId}"
               onclick="
                       event.preventDefault();
                       this.closest('form').answerId.value='${answer.id}';
                       this.closest('form').submit();
                       ">
                <button type="button" class="btn btn-primary m-2">
                        ${answer.text}
                </button>
            </a>
        </c:forEach>
    </div>
    <input type="hidden" name="answerId" value=""/>
</form>


<div class="text-center my-4">
    <img src="images/forest.jpg" class="img-fluid rounded" alt="Бородавочник">
</div>

</body>
</html>
