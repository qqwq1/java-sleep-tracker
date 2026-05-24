package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class SleepingSessionsCounterFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        String description = "Количество сессий сна за данный период";
        return new SleepAnalysisResult(description,  String.valueOf(sleepingSessions.size()));
    }
}
