import model.Answer;
import model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.QuestRepository;
import service.QuestService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class QuestServiceTest {
    private QuestService questService;

    @BeforeEach
    void setUp() {
        QuestRepository repo = new QuestRepository();
        questService = new QuestService(repo);
    }

    @Test
    void testGetQuestion_ValidId() {
        Question q = questService.getQuestionById(1L);
        assertNotNull(q);
        assertTrue(q.getText().startsWith("Ты просыпаешься у костра"));
    }

    @Test
    void testGetQuestion_InvalidId() {
        assertThrows(NoSuchElementException.class, () -> questService.getQuestionById(999L));
    }

    @Test
    void testChooseAnswer_1_11() {
        Question next = questService.chooseAnswer(1L, 11L);
        assertNotNull(next);
        assertEquals(2L, next.getId());
    }

    @Test
    void testChooseAnswer_1_12() {
        Question next = questService.chooseAnswer(1L, 12L);
        assertNotNull(next);
        assertEquals(3L, next.getId());
    }

    @Test
    void testChooseAnswer_3_31_Lose() {
        Question next = questService.chooseAnswer(3L, 31L);
        Answer a = questService.getAnswer(31L);
        assertNull(next); // конец квеста
        assertTrue(a.isLosing());
        assertEquals("Орки оказались сильнее. Ты погиб.", a.getEndText());
    }

    @Test
    void testChooseAnswer_3_32_Win() {
        Question next = questService.chooseAnswer(3L, 32L);
        Answer a = questService.getAnswer(32L);
        assertNull(next);
        assertTrue(a.isWinning());
        assertEquals("Тебе удалось сбежать, но приключение закончилось слишком быстро.", a.getEndText());
    }

    @Test
    void testChooseAnswer_InvalidAnswerForQuestion() {
        assertThrows(IllegalArgumentException.class, () -> questService.chooseAnswer(1L, 32L));
    }

    @Test
    void testChooseAnswer_NullNextQuestionIdWithoutWinLose() {
        Question question = new Question();
        question.setId(99L);
        Answer answer = new Answer();
        answer.setId(999L);
        answer.setNextQuestionId(null);
        question.setAnswers(List.of(answer));
        questService.addQuestionForTest(question);
        assertThrows(NullPointerException.class, () -> questService.chooseAnswer(99L, 999L));
    }

    @Test
    void testChooseAnswer_4_41_WinCase() {
        Question next = questService.chooseAnswer(4L, 41L);
        Answer answer = questService.getAnswer(41L);
        assertNull(next);
        assertTrue(answer.isWinning());
        assertEquals("Ты получаешь 10 медных монет и опыт. Победа!", answer.getEndText());
    }

    @Test
    void testChooseAnswer_5_52_LoseCase() {
        Question next = questService.chooseAnswer(5L, 52L);
        Answer answer = questService.getAnswer(52L);
        assertNull(next);
        assertTrue(answer.isLosing());
        assertEquals("Маг превратил тебя в овцу. Проигрыш!", answer.getEndText());
    }

    @Test
    void testHistoryTracking() {
        questService.clearHistory(); // если есть
        questService.chooseAnswer(1L, 11L); // step 1
        questService.chooseAnswer(2L, 21L); // step 2
        questService.chooseAnswer(4L, 41L); // step 3
        assertEquals(List.of(11L, 21L, 41L), questService.getHistory());
    }
}
