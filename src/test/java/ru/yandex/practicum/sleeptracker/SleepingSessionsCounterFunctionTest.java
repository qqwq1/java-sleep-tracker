package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class SleepingSessionsCounterFunctionTest {
    List<SleepingSession> sleepingSessionList;

    @BeforeEach
    public void init() {
        sleepingSessionList = new ArrayList<>();
        LocalDateTime start = LocalDateTime.of(26, 10, 10, 12, 30);
        LocalDateTime finish;
        SleepQuality sleepQuality;
        for (int i = 0; i < 5; i++) {
            finish = start.plusHours(i + 1);
            sleepQuality = SleepQuality.values()[i % 3];
            sleepingSessionList.add(new SleepingSession(start, finish, sleepQuality));
            start = start.plusDays(1);
        }
    }

    @Test
    @DisplayName("Корректный подсчет числа сессий сна")
    public void checkCorrectSleepingSessionsCount() {
        SleepAnalysisResult result = new SleepingSessionsCounterFunction().apply(sleepingSessionList);

        Assertions.assertEquals("5", result.result);
    }

    @Test
    @DisplayName("Результат функции содержит описание расчета")
    public void checkContainsNotEmptyDescription() {
        SleepAnalysisResult result = new SleepingSessionsCounterFunction().apply(sleepingSessionList);

        Assertions.assertFalse(result.description.isBlank());
    }

    @Test
    @DisplayName("Пустой список сессий возвращает 0")
    public void emptyReturnsZero() {
        SleepAnalysisResult result = new SleepingSessionsCounterFunction().apply(new ArrayList<>());

        Assertions.assertEquals("0", result.result);
    }
}
