package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.SleeplessNightCounterFunction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SleeplessNightCounterFunctionTest {

    private final SleeplessNightCounterFunction function = new SleeplessNightCounterFunction();

    @Test
    @DisplayName("Пустой список возвращает 0 бессонных ночей")
    public void testEmptyListReturnsZero() {
        SleepAnalysisResult result = function.apply(new ArrayList<>());

        assertEquals("0", result.result);
    }

    @Test
    @DisplayName("Сон с 23:00 до 07:00 следующего дня - нормальная ночь")
    public void testSleepFromEveningToMorning() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 23, 0),
                        LocalDateTime.of(2026, 5, 25, 7, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("0", result.result);
    }

    @Test
    @DisplayName("Сон только днем бессонная ночь")
    public void testDaySleepOnlyIsCountedAsSleepless() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 12, 0),
                        LocalDateTime.of(2026, 5, 24, 14, 0),
                        SleepQuality.BAD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("1", result.result);
    }

    @Test
    @DisplayName("Сон с 00:00 до 05:59 - нормальная ночь")
    public void testSleepFromMidnightToAlmostMorning() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 0, 0),
                        LocalDateTime.of(2026, 5, 24, 5, 59),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("0", result.result);
    }

    @Test
    @DisplayName("Сон с 06:00 до 08:00 - бессонная ночь")
    public void testSleepAfterSixIsCountedAsSleepless() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 6, 0),
                        LocalDateTime.of(2026, 5, 24, 8, 0),
                        SleepQuality.BAD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("1", result.result);
    }

    @Test
    @DisplayName("Дневной сон + ночной сон = нормальная ночь")
    public void testDaySleepAndNightSleepIsBothCountedAsNormal() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 12, 0),
                        LocalDateTime.of(2026, 5, 24, 14, 0),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 23, 0),
                        LocalDateTime.of(2026, 5, 25, 7, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("0", result.result);
    }

    @Test
    @DisplayName("сон начинается ровно в 00:00 - нормальная ночь")
    public void testSleepStartingAtMidnight() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 0, 0),
                        LocalDateTime.of(2026, 5, 24, 2, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("0", result.result);
    }

    @Test
    @DisplayName("сон заканчивается ровно в 06:00 - нормальная ночь")
    public void testSleepEndingAtSix() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 4, 0),
                        LocalDateTime.of(2026, 5, 24, 6, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("0", result.result);
    }

    @Test
    @DisplayName("сон начинается в 05:59 - нормальная ночь")
    public void testSleepStartingAtFiftyNinePM() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 5, 59),
                        LocalDateTime.of(2026, 5, 24, 8, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("0", result.result);
    }

    @Test
    @DisplayName("Несколько дней: 1 бессонная + 1 нормальная + 1 бессонная = 2 бессонных")
    public void testMultipleDays() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 23, 12, 0),
                        LocalDateTime.of(2026, 5, 23, 14, 0),
                        SleepQuality.BAD
                ),
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 23, 0),
                        LocalDateTime.of(2026, 5, 25, 7, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 25, 15, 0),
                        LocalDateTime.of(2026, 5, 25, 17, 0),
                        SleepQuality.BAD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("2", result.result);
    }

    @Test
    @DisplayName("Описание результата не пустое")
    public void testDescriptionIsNotBlank() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 12, 0),
                        LocalDateTime.of(2026, 5, 24, 14, 0),
                        SleepQuality.BAD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertFalse(result.description.isBlank());
        assertTrue(result.description.contains("бессонных"));
    }

    @Test
    @DisplayName("Очень длинный сон - нормальная ночь")
    public void testVeryLongSleepOverMultipleDays() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 23, 20, 0),
                        LocalDateTime.of(2026, 5, 25, 8, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);


        assertEquals("0", result.result);
    }

    @Test
    @DisplayName("Несколько нормальных сессий в один день:")
    public void testMultipleSessionsInOneDay() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 10, 0),
                        LocalDateTime.of(2026, 5, 24, 12, 0),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 2, 0),
                        LocalDateTime.of(2026, 5, 24, 4, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("0", result.result);
    }
}
