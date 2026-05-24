package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MeanSessionFinderFunctionTest {

    @Test
    public void testFindMeanSession() {
        LocalDateTime start1 = LocalDateTime.of(2024, 1, 1, 22, 0);
        LocalDateTime finish1 = LocalDateTime.of(2024, 1, 2, 8, 0);  // 10 часов

        LocalDateTime start2 = LocalDateTime.of(2024, 1, 2, 23, 0);
        LocalDateTime finish2 = LocalDateTime.of(2024, 1, 3, 4, 0);  // 5 часов

        LocalDateTime start3 = LocalDateTime.of(2024, 1, 3, 22, 0);
        LocalDateTime finish3 = LocalDateTime.of(2024, 1, 4, 8, 0);  // 10 часов

        List<SleepingSession> sessions = List.of(
                new SleepingSession(start1, finish1, SleepQuality.GOOD),
                new SleepingSession(start2, finish2, SleepQuality.NORMAL),
                new SleepingSession(start3, finish3, SleepQuality.GOOD)
        );

        MeanSessionFinderFunction function = new MeanSessionFinderFunction();


        SleepAnalysisResult result = function.apply(sessions);


        assertEquals("500.0", result.result);
    }

    @Test
    public void testFindMeanSessionEmptyList() {
        List<SleepingSession> sessions = List.of();
        MeanSessionFinderFunction function = new MeanSessionFinderFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("-1.0", result.result);
    }
}
