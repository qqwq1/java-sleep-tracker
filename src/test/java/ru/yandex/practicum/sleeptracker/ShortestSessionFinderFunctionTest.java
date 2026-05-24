package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShortestSessionFinderFunctionTest {

    @Test
    public void testFindShortestSession() {
        LocalDateTime start1 = LocalDateTime.of(2024, 1, 1, 22, 0);
        LocalDateTime finish1 = LocalDateTime.of(2024, 1, 2, 8, 0);  // 10 часов

        LocalDateTime start2 = LocalDateTime.of(2024, 1, 2, 23, 0);
        LocalDateTime finish2 = LocalDateTime.of(2024, 1, 3, 4, 0);  // 5 часов

        LocalDateTime start3 = LocalDateTime.of(2024, 1, 3, 22, 0);
        LocalDateTime finish3 = LocalDateTime.of(2024, 1, 4, 7, 30);  // 9.5 часов

        List<SleepingSession> sessions = List.of(
                new SleepingSession(start1, finish1, SleepQuality.GOOD),
                new SleepingSession(start2, finish2, SleepQuality.BAD),
                new SleepingSession(start3, finish3, SleepQuality.NORMAL)
        );

        ShortestSessionFinderFunction function = new ShortestSessionFinderFunction();


        SleepAnalysisResult result = function.apply(sessions);


        assertEquals("300", result.result);
    }

    @Test
    public void testFindShortestSessionEmptyList() {
        List<SleepingSession> sessions = List.of();
        ShortestSessionFinderFunction function = new ShortestSessionFinderFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("-1", result.result);
    }
}
