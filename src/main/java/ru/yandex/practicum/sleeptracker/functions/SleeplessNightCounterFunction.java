package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SleeplessNightCounterFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final int NIGHT_END_HOUR = 6;
    private final static String DESCRIPTION = "Количество бессонных ночей за данный период";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        Map<LocalDate, List<SleepingSession>> sleepingSessionsByDay = sleepingSessions.stream()
                .collect(Collectors.groupingBy(session -> session.start.toLocalDate()));

        long sleeplessNights = sleepingSessionsByDay.values().stream()
                .filter(daySessions -> !isSleepDuringNight(daySessions))
                .count();

        return new SleepAnalysisResult(DESCRIPTION, String.valueOf(sleeplessNights));
    }

    private boolean isSleepDuringNight(List<SleepingSession> daySessions) {
        return daySessions.stream()
                .anyMatch(session -> {
                    LocalDateTime start = session.start;
                    LocalDateTime begin = start.minusHours(start.getHour()).minusMinutes(start.getMinute());
                    LocalDateTime end = begin.plusHours(NIGHT_END_HOUR);
                    return (session.start.toLocalDate().isBefore(session.finish.toLocalDate()))
                            || (start.equals(begin) || (start.isAfter(begin) && start.isBefore(end)));
                });
    }
}
