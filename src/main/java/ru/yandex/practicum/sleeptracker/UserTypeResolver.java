package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserTypeResolver implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final int END_OF_NIGHT = 6;
    private static final int OWL_SLEEP_FROM = 23;
    private static final int OWL_SLEEP_TO = 9;
    private static final int EARLY_SLEEP_FROM = 22;
    private static final int EARLY_SLEEP_TO = 7;


    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        Map<UserType, Long> types = sleepingSessions.stream()
                .filter(session -> {
                    LocalDateTime start = session.start;
                    LocalDateTime firstDayStart = start.minusHours(start.getHour()).minusMinutes(start.getMinute());
                    LocalDateTime firstDayEnd = firstDayStart.plusHours(END_OF_NIGHT);

                    LocalDateTime finish = session.finish;
                    LocalDateTime secondDayStart;
                    LocalDateTime secondDayEnd;
                    if (!start.toLocalDate().equals(finish.toLocalDate())) {
                        secondDayStart = finish.minusHours(finish.getHour()).minusMinutes(finish.getMinute());
                        secondDayEnd = secondDayStart.plusHours(END_OF_NIGHT);
                    } else {
                        secondDayStart = firstDayStart;
                        secondDayEnd = firstDayEnd;
                    }

                    return (intervalsOverlap(firstDayStart, firstDayEnd, start, finish)
                            || intervalsOverlap(secondDayStart, secondDayEnd, start, finish));
                })
                .collect(Collectors.groupingBy(this::classify, Collectors.counting()));

        UserType maxType = types.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(UserType.DAY_BIRD);
        String description = "Тип пользователя за данный период";
        return new SleepAnalysisResult(description, maxType.name());
    }

    private boolean intervalsOverlap(LocalDateTime interval1Start, LocalDateTime interval1Finish,
                                     LocalDateTime interval2Start, LocalDateTime interval2Finish) {
        return interval1Start.isBefore(interval2Finish) && interval1Finish.isAfter(interval2Start);
    }

    private UserType classify(SleepingSession session) {
        LocalDateTime start = session.start;
        LocalDateTime finish = session.finish;

        boolean owl = start.toLocalTime().isAfter(LocalTime.of(OWL_SLEEP_FROM, 0))
                && finish.toLocalTime().isAfter(LocalTime.of(OWL_SLEEP_TO, 0));

        boolean earlyBird = start.toLocalTime().isBefore(LocalTime.of(EARLY_SLEEP_FROM, 0))
                && finish.toLocalTime().isBefore(LocalTime.of(EARLY_SLEEP_TO, 0));

        if (owl) {
            return UserType.NIGHT_OWL;
        }
        if (earlyBird) {
            return UserType.EARLY_BIRD;
        }
        return UserType.DAY_BIRD;
    }


}
