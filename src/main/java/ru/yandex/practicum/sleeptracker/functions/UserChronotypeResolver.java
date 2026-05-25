package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.Chronotype;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserChronotypeResolver implements Function<List<SleepingSession>, SleepAnalysisResult> {
    private static final int END_OF_NIGHT = 6;
    private static final int OWL_SLEEP_FROM = 23;
    private static final int OWL_SLEEP_TO = 9;
    private static final int EARLY_SLEEP_FROM = 22;
    private static final int EARLY_SLEEP_TO = 7;
    private final static String DESCRIPTION = "Тип пользователя за данный период";


    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        Map<Chronotype, Long> types = sleepingSessions.stream()
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

        Chronotype maxType = types.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Chronotype.DAY_BIRD);
        return new SleepAnalysisResult(DESCRIPTION, maxType.name());
    }

    private boolean intervalsOverlap(LocalDateTime interval1Start, LocalDateTime interval1Finish,
                                     LocalDateTime interval2Start, LocalDateTime interval2Finish) {
        return interval1Start.isBefore(interval2Finish) && interval1Finish.isAfter(interval2Start);
    }

    private Chronotype classify(SleepingSession session) {
        LocalDateTime start = session.start;
        LocalDateTime finish = session.finish;

        boolean owl = start.toLocalTime().isAfter(LocalTime.of(OWL_SLEEP_FROM, 0))
                && finish.toLocalTime().isAfter(LocalTime.of(OWL_SLEEP_TO, 0));

        boolean earlyBird = start.toLocalTime().isBefore(LocalTime.of(EARLY_SLEEP_FROM, 0))
                && finish.toLocalTime().isBefore(LocalTime.of(EARLY_SLEEP_TO, 0));

        if (owl) {
            return Chronotype.NIGHT_OWL;
        }
        if (earlyBird) {
            return Chronotype.EARLY_BIRD;
        }
        return Chronotype.DAY_BIRD;
    }


}
