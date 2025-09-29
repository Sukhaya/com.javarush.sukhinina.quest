package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Answer;
import model.Quest;
import model.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class QuestRepository {
    private static final Logger logger = LogManager.getLogger(QuestRepository.class);

    private final Map<Long, Question> questions = new HashMap<>();
    private Map<Long, Answer> answers = new HashMap<>();
    private Quest quest;

    public Quest getQuest() {
        return quest;
    }

    public QuestRepository() {
        loadFromJson();
    }

    public Map<Long, Question> getQuestions() {
        return questions;
    }

    public Map<Long, Answer> getAnswers() {
        return answers;
    }

    private void loadFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream in = getClass().getClassLoader().getResourceAsStream("quest.json");
            if (in == null) {
                logger.error("File quest.json not found in resources\"");
                throw new IllegalStateException("quest.json not found in resources");
            }

            this.quest = mapper.readValue(in, Quest.class);

            System.out.println("Quest loaded: " + quest);
            System.out.println("Questions: " + (quest.getQuestions() != null
                    ? quest.getQuestions().size()
                    : "null"));

            if (quest.getQuestions() != null) {
                for (Question q : quest.getQuestions()) {
                    questions.put(q.getId(), q);

                    if (q.getAnswers() != null) {
                        for (Answer a : q.getAnswers()) {
                            logger.debug("Answer parsed: id={}, text={}", a.getId(), a.getText());
                            answers.put(a.getId(), a);
                        }
                    }
                }
            }

            logger.info("Quest loaded: '{}' ({} Questions, {} Answers)",
                    quest.getTitle(),
                    questions.size(),
                    answers.size()
            );

        } catch (IOException e) {
            logger.error("Error load quest.json", e);
        }
    }

    public Question getQuestionById(Long id) {
        Question q = questions.get(id);
        if (q == null) {
            logger.warn("Question with id {} not found in resources", id);
        } else {
            logger.debug("Question loaded from repo: id={}, text='{}'", q.getId(), q.getText());
        }
        return q;
    }
}
