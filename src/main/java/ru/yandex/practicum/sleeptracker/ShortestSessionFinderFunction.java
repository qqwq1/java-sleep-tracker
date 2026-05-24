package ru.yandex.practicum.sleeptracker;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class ShortestSessionFinderFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        long result = sleepingSessions.stream()
                .min(Comparator.comparing(s -> s.duration))
                .map(s -> s.duration.toMinutes())
                .orElse(-1L);
        String description = "Минимальная продолжительность сессии сна за данный период";
        return new SleepAnalysisResult(description, Long.toString(result));
    }
}
