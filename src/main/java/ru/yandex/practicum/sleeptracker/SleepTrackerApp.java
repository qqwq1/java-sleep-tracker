package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {
    private static final List<Function<List<SleepingSession>, SleepAnalysisResult>> functionList = new ArrayList<>();

    public static void main(String[] args) {
        String sleepLogFilePath = args[0];
        List<SleepingSession> sleepingSessions;
        fillFunctionList();
        try {
            sleepingSessions = SleepingSessionsLoader.readLogToSleepingSessions(sleepLogFilePath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        functionList.forEach(f -> {
                    SleepAnalysisResult result = f.apply(sleepingSessions);
                    System.out.println(result.description + " -> " + result.result);
                }
        );
    }

    public static void fillFunctionList() {
        functionList.addAll(
                List.of(new SleepingSessionsCounterFunction(),
                        new ShortestSessionFinderFunction(),
                        new LongestSessionFinderFunction(),
                        new MeanSessionFinderFunction(),
                        new SleeplessNightCounterFunction(),
                        new UserTypeResolver()
                ));
    }
}