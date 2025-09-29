import model.Answer;
import model.Quest;
import model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.QuestRepository;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class QuestRepositoryTest {
    private QuestRepository repository;

    @BeforeEach
    void setUp() {
        repository = new QuestRepository();
    }

    @Test
    void testQuestIsLoaded() {
        Quest quest = repository.getQuest();
        assertNotNull(quest, "Quest должен быть загружен из JSON");
        assertEquals("Пробуждение в Эльвиннском лесу", quest.getTitle(), "Название квеста должно совпадать");
        assertEquals(5, quest.getQuestions().size(), "Количество вопросов должно быть 5");
    }

    @Test
    void testGetQuestions() {
        Map<Long, Question> questions = repository.getQuestions();
        assertNotNull(questions, "Questions map не должна быть null");
        assertEquals(5, questions.size(), "Количество вопросов должно соответствовать JSON");

        Question q1 = questions.get(1L);
        assertNotNull(q1);
        assertTrue(q1.getText().startsWith("Ты просыпаешься у костра"), "Текст вопроса #1 должен быть корректным");
    }

    @Test
    void testGetAnswers() {
        Map<Long, Answer> answers = repository.getAnswers();
        assertNotNull(answers, "Answers map не должна быть null");
        assertEquals(9, answers.size(), "Всего должно быть 9 ответов");

        Answer a12 = answers.get(12L);
        assertNotNull(a12);
        assertEquals("Отказаться и уйти в лес.", a12.getText());
        assertEquals(3L, a12.getNextQuestionId());
    }

    @Test
    void testGetQuestionByIdExisting() {
        Question q1 = repository.getQuestionById(1L);
        assertNotNull(q1);
        assertEquals(1L, q1.getId());
        assertEquals(2, q1.getAnswers().size(), "Вопрос #1 должен содержать два ответа");
    }

    @Test
    void testGetQuestionByIdNonExisting() {
        Question q999 = repository.getQuestionById(999L);
        assertNull(q999, "Для несуществующего id метод должен возвращать null");
    }

    @Test
    void testAnswersFromQuestions() {
        for (Question q : repository.getQuestions().values()) {
            for (Answer a : q.getAnswers()) {
                assertTrue(repository.getAnswers().containsKey(a.getId()),
                        "Ответ id=" + a.getId() + " должен быть в answers map");
            }
        }
    }
}
