package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class MeanSessionFinderFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        double result = sleepingSessions.stream()
                .mapToLong(s->s.duration.toMinutes())
                .average()
                .orElse(-1);
        String description = "Средняя продолжительность сна за данный период";
        return new SleepAnalysisResult(description, Double.toString(result));
    }
}
