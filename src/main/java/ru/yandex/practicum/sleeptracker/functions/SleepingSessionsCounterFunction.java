package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class SleepingSessionsCounterFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private final static String DESCRIPTION = "Количество сессий сна за данный период";
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        return new SleepAnalysisResult(DESCRIPTION,  String.valueOf(sleepingSessions.size()));
    }
}
