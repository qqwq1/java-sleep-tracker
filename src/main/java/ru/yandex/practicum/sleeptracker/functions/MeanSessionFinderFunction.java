package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class MeanSessionFinderFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final String DESCRIPTION = "Средняя продолжительность сна за данный период";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        double result = sleepingSessions.stream()
                .mapToLong(s -> s.duration.toMinutes())
                .average()
                .orElse(0);
        return new SleepAnalysisResult(DESCRIPTION, Double.toString(result));
    }
}
