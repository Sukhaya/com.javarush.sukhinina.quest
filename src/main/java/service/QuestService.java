package service;

import model.Answer;
import model.Quest;
import model.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.QuestRepository;

import java.util.*;

public class QuestService {
    private static final Logger logger = LogManager.getLogger(QuestService.class);

    private final QuestRepository repository;
    private Map<Long, Question> questions;
    private Map<Long, Answer> answers;
    private final List<Long> history = new ArrayList<>();

    public QuestService(QuestRepository repository) {
        this.repository = Objects.requireNonNull(repository, "Repository must not be null");
        this.questions = new HashMap<>(repository.getQuestions());
        this.answers = new HashMap<>(repository.getAnswers());
    }

    public void clearHistory() {
        history.clear();
    }

    public List<Long> getHistory() {
        return new ArrayList<>(history);
    }

    public Set<Long> getAllAnswerKeys() {
        return answers.keySet();
    }

    public Answer getAnswer(Long answerId) {
        return answers.get(answerId);
    }

    public Question getQuestionById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Question id is null");
        }
        Question q = repository.getQuestionById(id);
        if (q == null) {
            logger.error("Question with id {} not found", id);
            throw new NoSuchElementException("Question is not found: " + id);
        }
        logger.debug("Question received with id {}: {}", id, q.getText());
        return q;
    }

    //для возможной будущей реализации
    public Long getFirstQuestionId() {
        Quest quest = repository.getQuest();
        if (quest == null || quest.getQuestions() == null || quest.getQuestions().isEmpty()) {
            logger.error("Quest has no questions");
            throw new IllegalStateException("Quest has no questions");
        }
        Long firstId = quest.getQuestions().get(0).getId();
        logger.debug("Fists question has id {}", firstId);
        return firstId;
    }

    public Question chooseAnswer(Long currentQuestionId, Long answerId) {
        Question current = repository.getQuestionById(currentQuestionId);

        Answer chosen = current.getAnswers().stream()
                .filter(a -> a.getId().equals(answerId))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Answer with id {} is not found in question {}", answerId, currentQuestionId);
                    return new IllegalArgumentException("No such answer with id " + answerId);
                });

        logger.info("User choosed answer id={} (text='{}')", history.add(chosen.getId()), chosen.getText());

        if (chosen.isWinning() || chosen.isLosing()) {
            logger.info("Answer finishes quest (isWinning={}, isLosing={})", chosen.isWinning(), chosen.isLosing());
            return null;
        }

        Question next = repository.getQuestionById(chosen.getNextQuestionId());
        if (next == null) {
            logger.error("Next question with id {} is not found", chosen.getNextQuestionId());
        } else {
            logger.debug("Go to the question id={} (text='{}')", next.getId(), next.getText());
        }

        return next;
    }

    public Question getNextQuestion(Long answerId) {
        Answer answer = answers.get(answerId);
        if (answer == null) {
            logger.error("Answer with id {} is not found", answerId);
            return null;
        }
        Long nextId = answer.getNextQuestionId();
        if (nextId == null) {
            logger.warn("The next question for answer id={} is not asked", answerId);
            return null;
        }

        if (answer.isWinning() || answer.isLosing() || answer.getNextQuestionId() == null) {
            return null; // конец квеста, дальше нет вопроса
        }

        Question next = questions.get(nextId);
        if (next == null) {
            logger.error("Question with id {} (for answer {}) is not found", nextId, answerId);
        } else {
            logger.debug("The following question was found: id={} (text='{}')", next.getId(), next.getText());
        }

        return next;
    }

    public void addQuestionForTest(Question question) {
        if (question != null) {
            questions.put(question.getId(), question);
            if (question.getAnswers() != null) {
                for (Answer a : question.getAnswers()) {
                    answers.put(a.getId(), a);
                }
            }
        }
    }
}
