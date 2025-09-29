import model.Answer;
import model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.QuestService;
import web.QuestServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class QuestServletTest {

    @InjectMocks
    private QuestServlet servlet;

    @Mock
    private QuestService questService;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private RequestDispatcher dispatcher;
    @Mock
    private ServletContext servletContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(servletContext.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        servlet = new QuestServlet() {
            @Override
            public ServletContext getServletContext() {
                return servletContext;
            }
        };

        servlet.questService = questService;
    }

    @Test
    void testDoGet_withQuestionId() throws Exception {
        Question mockQuestion = new Question();
        mockQuestion.setId(3L);
        mockQuestion.setText("Ты уходишь в лес и сталкиваешься с орками-разведчиками. Они замечают тебя.");

        when(req.getParameter("questionId")).thenReturn("3");
        when(questService.getQuestionById(3L)).thenReturn(mockQuestion);
        when(servletContext.getRequestDispatcher("/quest.jsp")).thenReturn(dispatcher);

        servlet.doGet(req, resp);

        verify(req).setAttribute(eq("question"), eq(mockQuestion));
        verify(dispatcher).forward(req, resp);
    }

    @Test
    void testDoGet_withoutQuestionId_defaultsTo1() throws Exception {
        Question mockQuestion = new Question();
        mockQuestion.setId(1L);
        mockQuestion.setText("Default question");

        when(req.getParameter("questionId")).thenReturn(null);
        when(questService.getQuestionById(1L)).thenReturn(mockQuestion);
        when(servletContext.getRequestDispatcher("/quest.jsp")).thenReturn(dispatcher);

        servlet.doGet(req, resp);

        verify(req).setAttribute(eq("question"), eq(mockQuestion));
        verify(dispatcher).forward(req, resp);
    }

    @Test
    void testDoPost_finalAnswer_forwardsToEnd() throws Exception {
        Answer finalAnswer = new Answer();
        finalAnswer.setId(100L);
        finalAnswer.setWinning(true);
        finalAnswer.setEndText("You win!");

        when(req.getParameter("answerId")).thenReturn("100");
        when(questService.getAnswer(100L)).thenReturn(finalAnswer);
        when(servletContext.getRequestDispatcher("/end.jsp")).thenReturn(dispatcher);

        servlet.doPost(req, resp);

        verify(req).setAttribute("endText", "You win!");
        verify(req).setAttribute("isWinning", true);
        verify(req).setAttribute("isLosing", false);
        verify(dispatcher).forward(req, resp);
    }

    @Test
    void testDoPost_redirectsToNextQuestion() throws Exception {
        Answer answer = new Answer();
        answer.setId(200L);
        answer.setNextQuestionId(2L);

        Question nextQ = new Question();
        nextQ.setId(2L);

        when(req.getParameter("answerId")).thenReturn("200");
        when(questService.getAnswer(200L)).thenReturn(answer);
        when(questService.getNextQuestion(200L)).thenReturn(nextQ);
        when(req.getContextPath()).thenReturn("/app");

        servlet.doPost(req, resp);

        verify(resp).sendRedirect("/app/quest?questionId=2");
    }
}
