package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class ShortestSessionFinderFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final String DESCRIPTION = "Минимальная продолжительность сессии сна за данный период";
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        long result = sleepingSessions.stream()
                .min(Comparator.comparing(s -> s.duration))
                .map(s -> s.duration.toMinutes())
                .orElse(0L);
        return new SleepAnalysisResult(DESCRIPTION, Long.toString(result));
    }
}
