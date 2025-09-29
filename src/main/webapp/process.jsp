<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%
    request.setCharacterEncoding("UTF-8");
    String userName = request.getParameter("userName");

    if (userName != null && !userName.isEmpty()) {
        session.setAttribute("userName", userName);
        response.sendRedirect("quest?questionId=1");
    } else {
        response.sendRedirect("start.jsp?error=empty");
    }
%>