package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SleepingSession {
    public final SleepQuality sleepQuality;
    public final LocalDateTime start;
    public final LocalDateTime finish;
    public final Duration duration;

    public SleepingSession(LocalDateTime start, LocalDateTime finish, SleepQuality sleepQuality) {
        this.finish = finish;
        this.start = start;
        this.sleepQuality = sleepQuality;
        this.duration = Duration.between(start, finish);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SleepingSession that = (SleepingSession) o;
        return sleepQuality == that.sleepQuality && Objects.equals(start, that.start) && Objects.equals(finish, that.finish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sleepQuality, start, finish);
    }
}
