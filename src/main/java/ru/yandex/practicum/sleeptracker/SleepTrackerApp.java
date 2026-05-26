package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.functions.*;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {
    private static final List<Function<List<SleepingSession>, SleepAnalysisResult>> ANALYTIC_FUNCTIONS = List.of(
            new SleepingSessionsCounterFunction(),
            new ShortestSessionFinderFunction(),
            new LongestSessionFinderFunction(),
            new MeanSessionFinderFunction(),
            new BadSleepQualityFinderFunction(),
            new SleeplessNightCounterFunction(),
            new UserChronotypeResolver()
    );

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Укажите путь к файлу с логом сна в аргументах запуска.");
            return;
        }
        String sleepLogFilePath = args[0];
        List<SleepingSession> sleepingSessions;
        try {
            sleepingSessions = SleepingSessionsLoader.readLogToSleepingSessions(sleepLogFilePath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        ANALYTIC_FUNCTIONS.forEach(f -> {
                    SleepAnalysisResult result = f.apply(sleepingSessions);
                    System.out.println(result.description + " -> " + result.result);
                }
        );
    }
}