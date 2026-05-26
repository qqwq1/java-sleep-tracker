package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.LongestSessionFinderFunction;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LongestSessionFinderFunctionTest {

    @Test
    public void testFindLongestSession() {
        LocalDateTime start1 = LocalDateTime.of(2024, 1, 1, 22, 0);
        LocalDateTime finish1 = LocalDateTime.of(2024, 1, 2, 8, 0);  // 10 часов

        LocalDateTime start2 = LocalDateTime.of(2024, 1, 2, 23, 0);
        LocalDateTime finish2 = LocalDateTime.of(2024, 1, 3, 4, 0);  // 5 часов

        LocalDateTime start3 = LocalDateTime.of(2024, 1, 3, 20, 0);
        LocalDateTime finish3 = LocalDateTime.of(2024, 1, 4, 10, 0);  // 14 часов

        List<SleepingSession> sessions = List.of(
                new SleepingSession(start1, finish1, SleepQuality.GOOD),
                new SleepingSession(start2, finish2, SleepQuality.NORMAL),
                new SleepingSession(start3, finish3, SleepQuality.GOOD)
        );

        LongestSessionFinderFunction function = new LongestSessionFinderFunction();


        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("840", result.result);  // 14 часов = 840 минут
    }

    @Test
    public void testFindLongestSessionEmptyList() {
        List<SleepingSession> sessions = List.of();
        LongestSessionFinderFunction function = new LongestSessionFinderFunction();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals("0", result.result);
    }
}
