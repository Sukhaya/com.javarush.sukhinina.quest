import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import web.InitServlet;
import web.MockServletConfig;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InitServletTest {

    @InjectMocks
    private InitServlet servlet;

    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private ServletContext servletContext;
    @Mock
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        when(servletContext.getRequestDispatcher("/start.jsp")).thenReturn(dispatcher);
    }

    @Test
    void testDoGet_forwardsToStartJsp() throws Exception {
        servlet.init(new MockServletConfig(servletContext));

        when(servletContext.getRequestDispatcher("/start.jsp")).thenReturn(dispatcher);
        when(req.getMethod()).thenReturn("GET");

        servlet.service(req, resp);

        verify(servletContext).getRequestDispatcher("/start.jsp");
        verify(dispatcher).forward(req, resp);
    }
}
