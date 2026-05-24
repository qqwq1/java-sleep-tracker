package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTypeResolverTest {

    private final UserTypeResolver resolver = new UserTypeResolver();

    @Test
    @DisplayName("пустой список — Голубь")
    public void emptyList_returns_DAY_BIRD() {
        SleepAnalysisResult result = resolver.apply(List.of());
        assertEquals(UserType.DAY_BIRD.name(), result.result);
    }

    @Test
    @DisplayName("одна сессия (23:30–09:30) — Сова")
    public void singleOwlSession_returns_NIGHT_OWL() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 23, 30),
                        LocalDateTime.of(2026, 5, 25, 9, 30),
                        SleepQuality.GOOD
                )
        );
        SleepAnalysisResult result = resolver.apply(sessions);
        assertEquals(UserType.NIGHT_OWL.name(), result.result);
    }

    @Test
    @DisplayName("одна сессия (21:00–06:30) — Жаворонок")
    public void singleEarlyBirdSession_returns_EARLY_BIRD() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 21, 0),
                        LocalDateTime.of(2026, 5, 25, 6, 30),
                        SleepQuality.NORMAL
                )
        );
        SleepAnalysisResult result = resolver.apply(sessions);
        assertEquals(UserType.EARLY_BIRD.name(), result.result);
    }

    @Test
    @DisplayName("только дневная сессия — Голубь")
    public void daytimeSessionOnly_returns_DAY_BIRD() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 12, 0),
                        LocalDateTime.of(2026, 5, 24, 14, 0),
                        SleepQuality.BAD
                )
        );
        SleepAnalysisResult result = resolver.apply(sessions);
        assertEquals(UserType.DAY_BIRD.name(), result.result);
    }

    @Test
    @DisplayName("большинство сов — Сова")
    public void majorityOwl_returns_NIGHT_OWL() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 23, 30),
                        LocalDateTime.of(2026, 5, 25, 10, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 25, 23, 45),
                        LocalDateTime.of(2026, 5, 26, 9, 15),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 26, 21, 0),
                        LocalDateTime.of(2026, 5, 27, 6, 0),
                        SleepQuality.NORMAL
                )
        );
        SleepAnalysisResult result = resolver.apply(sessions);
        assertEquals(UserType.NIGHT_OWL.name(), result.result);
    }

    @Test
    @DisplayName("несколько жаворонков — Жаворонок")
    public void multipleEarlyBirds_returns_EARLY_BIRD() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 21, 0),
                        LocalDateTime.of(2026, 5, 25, 6, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 25, 20, 30),
                        LocalDateTime.of(2026, 5, 26, 6, 15),
                        SleepQuality.NORMAL
                )
        );
        SleepAnalysisResult result = resolver.apply(sessions);
        assertEquals(UserType.EARLY_BIRD.name(), result.result);
    }

    @Test
    @DisplayName("короткая ночная сессия — Голубь")
    public void shortNightSession_classified_as_DAY_BIRD() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2026, 5, 24, 23, 0),
                        LocalDateTime.of(2026, 5, 25, 1, 0),
                        SleepQuality.GOOD
                )
        );
        SleepAnalysisResult result = resolver.apply(sessions);
        assertEquals(UserType.DAY_BIRD.name(), result.result);
    }
}
