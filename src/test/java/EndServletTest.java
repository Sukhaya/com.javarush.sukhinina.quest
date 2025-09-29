import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import web.EndServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

public class EndServletTest {

    private EndServlet servlet;

    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher dispatcher;
    @Mock
    private ServletContext servletContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(servletContext.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        servlet = new EndServlet() {
            @Override
            public ServletContext getServletContext() {
                return servletContext;
            }
        };

        when(req.getSession()).thenReturn(session);
    }

    @Test
    void testDoGet_forwardsToEndJsp_withEndText() throws Exception {
        when(session.getAttribute("endText")).thenReturn("К сожалению, ты проиграл.");

        servlet.doGet(req, resp);

        verify(req).setAttribute("endText", "К сожалению, ты проиграл.");
        verify(dispatcher).forward(req, resp);
    }

    @Test
    void testDoGet_noEndTextStillForwards() throws Exception {
        when(session.getAttribute("endText")).thenReturn(null);

        servlet.doGet(req, resp);

        verify(req).setAttribute("endText", null);
        verify(dispatcher).forward(req, resp);
    }
}

