package web;

import model.Answer;
import model.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.QuestRepository;
import service.QuestService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class QuestServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(QuestServlet.class);
    public QuestService questService;

    @Override
    public void init() {
        questService = new QuestService(new QuestRepository());
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String qid = req.getParameter("questionId");
        Long currentQuestionId;

        if (qid != null) {
            currentQuestionId = Long.valueOf(qid);
        } else {
            currentQuestionId = 1L;
        }

        Question current = questService.getQuestionById(currentQuestionId);
        req.setAttribute("question", current);

        getServletContext()
                .getRequestDispatcher("/quest.jsp")
                .forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String answerIdParam = req.getParameter("answerId");
        if (answerIdParam != null) {
            Long answerId = Long.valueOf(answerIdParam);

            logger.debug("== DEBUG ==");
            logger.debug("answerIdParam: '{}'", answerIdParam);
            logger.debug("answerId (Long): {}", answerId);
            logger.debug("All Keys in answers: {}", questService.getAllAnswerKeys());
            logger.debug("================");

            Answer chosen = questService.getAnswer(answerId);

            if (chosen == null) {
                throw new IllegalArgumentException("Ответ не найден: " + answerId);
            }

            // если финал
            if (chosen.isWinning() || chosen.isLosing() || chosen.getNextQuestionId() == null) {
                req.setAttribute("endText", chosen.getEndText());
                req.setAttribute("isWinning", chosen.isWinning());
                req.setAttribute("isLosing", chosen.isLosing());

                getServletContext()
                        .getRequestDispatcher("/end.jsp")
                        .forward(req, resp);
                return;
            }

            Question next = questService.getNextQuestion(answerId);

            if (next != null) {
                resp.sendRedirect(req.getContextPath() + "/quest?questionId=" + next.getId());
            } else {
                resp.sendRedirect(req.getContextPath() + "/quest?end=true");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/quest");
        }
    }
}
