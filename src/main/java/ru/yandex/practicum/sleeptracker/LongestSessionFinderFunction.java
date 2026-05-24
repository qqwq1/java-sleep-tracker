package ru.yandex.practicum.sleeptracker;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class LongestSessionFinderFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        long result = sleepingSessions.stream()
                .max(Comparator.comparing(s -> s.duration))
                .map(s -> s.duration.toMinutes())
                .orElse(-1L);
        String description = "Максимальная продолжительность сессии сна за данный период";
        return new SleepAnalysisResult(description, Long.toString(result));
    }
}
