package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.BadSleepQualityFinderFunction;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BadSleepQualityFinderFunctionTest {

    private final BadSleepQualityFinderFunction function = new BadSleepQualityFinderFunction();

    @Test
    @DisplayName("считает количество сессий с качеством BAD")
    public void countsBadSessions() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 1, 23, 0),
                        LocalDateTime.of(2026, 5, 2, 7, 0),
                        SleepQuality.BAD
                ),
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 2, 23, 0),
                        LocalDateTime.of(2026, 5, 3, 7, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 3, 23, 0),
                        LocalDateTime.of(2026, 5, 4, 7, 0),
                        SleepQuality.BAD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("2", result.result);
    }

    @Test
    @DisplayName("если BAD нет — возвращает 0")
    public void returnsZeroWhenNoBadSessions() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 1, 23, 0),
                        LocalDateTime.of(2026, 5, 2, 7, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 2, 13, 0),
                        LocalDateTime.of(2026, 5, 2, 14, 0),
                        SleepQuality.NORMAL
                )
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("0", result.result);
    }

    @Test
    @DisplayName("пустой список — возвращает 0")
    public void emptyListReturnsZero() {
        SleepAnalysisResult result = function.apply(List.of());
        assertEquals("0", result.result);
    }

    @Test
    @DisplayName("описание результата корректное и не пустое")
    public void descriptionIsCorrect() {
        SleepAnalysisResult result = function.apply(List.of());

        assertNotNull(result.description);
        assertFalse(result.description.isBlank());
        assertEquals("Количество сессий сна с плохим качеством за данный период", result.description);
    }
}
