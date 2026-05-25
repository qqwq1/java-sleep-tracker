package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class BadSleepQualityFinderFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private final static String DESCRIPTION = "Количество сессий сна с плохим качеством за данный период";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        long badSleepingSessionsCount = sleepingSessions.stream()
                .filter(session -> session.sleepQuality== SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult(DESCRIPTION,String.valueOf(badSleepingSessionsCount));
    }
}
