package web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EndServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String endText = (String) req.getSession().getAttribute("endText");
        req.setAttribute("endText", endText);
        getServletContext().getRequestDispatcher("/end.jsp").forward(req, resp);
    }
}
