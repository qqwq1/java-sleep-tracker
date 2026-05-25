package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class SleepingSessionsLoaderTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("валидный файл читается в список сессий")
    public void readsValidFile() throws Exception {
        Path file = tempDir.resolve("valid_log.txt");
        Files.write(file, List.of(
                "01.01.26 22:00;02.01.26 06:00;GOOD",
                "02.01.26 23:00;03.01.26 07:00;NORMAL"
        ), StandardCharsets.UTF_8);

        List<SleepingSession> sessions = SleepingSessionsLoader.readLogToSleepingSessions(file.toString());

        assertEquals(2, sessions.size());
        assertNotNull(sessions.get(0));
        assertNotNull(sessions.get(1));

        SleepingSession first = sessions.get(0);
        assertEquals(SleepQuality.GOOD, first.sleepQuality);
        assertEquals(LocalDateTime.of(2026, 1, 1, 22, 0), first.start);
        assertEquals(LocalDateTime.of(2026, 1, 2, 6, 0), first.finish);
        assertEquals(480L, first.duration.toMinutes());

        SleepingSession second = sessions.get(1);
        assertEquals(SleepQuality.NORMAL, second.sleepQuality);
        assertEquals(480L, second.duration.toMinutes());
    }

    @Test
    @DisplayName("пустые строки игнорируются")
    public void ignoresBlankLines() throws Exception {
        Path file = tempDir.resolve("with_blank_lines.txt");
        Files.write(file, List.of(
                "",
                "01.01.26 22:00;02.01.26 06:00;GOOD",
                "   ",
                "\t",
                "02.01.26 23:00;03.01.26 07:00;NORMAL",
                ""
        ), StandardCharsets.UTF_8);

        List<SleepingSession> sessions = SleepingSessionsLoader.readLogToSleepingSessions(file.toString());

        assertEquals(2, sessions.size());
        assertTrue(sessions.stream().allMatch(Objects::nonNull));
    }

    @Test
    @DisplayName("если файл не найден — выбрасывается FileNotFoundException с сообщением")
    public void throwsFileNotFound() {
        String missing = tempDir.resolve("no_such_file.txt").toString();

        FileNotFoundException ex = assertThrows(FileNotFoundException.class,
                () -> SleepingSessionsLoader.readLogToSleepingSessions(missing));

        assertTrue(ex.getMessage().contains("Не удалось найти файл по указанному пути ->"));
    }

    @Test
    @DisplayName("некорректная дата не падает, печатает сообщение и возвращает null в списке")
    public void invalidDatePrintsMessageAndAddsNull() throws Exception {
        Path file = tempDir.resolve("bad_date.txt");
        Files.write(file, List.of(
                "invalid-date;02.01.26 06:00;GOOD"
        ), StandardCharsets.UTF_8);

        String out = captureStdout(() -> {
            try {
                SleepingSessionsLoader.readLogToSleepingSessions(file.toString());
            } catch (Exception e) {
                fail("Исключение не ожидается при некорректной строке: " + e);
            }
        });

        List<SleepingSession> sessions = SleepingSessionsLoader.readLogToSleepingSessions(file.toString());
        assertEquals(1, sessions.size());
        assertNull(sessions.get(0));
        assertTrue(out.contains("Пропущена некорректная строка:"), "Ожидается сообщение о пропуске строки");
    }

    @Test
    @DisplayName("некорректный enum не падает, печатает сообщение и возвращает null в списке")
    public void invalidEnumPrintsMessageAndAddsNull() throws Exception {
        Path file = tempDir.resolve("bad_enum.txt");
        Files.write(file, List.of(
                "01.01.26 22:00;02.01.26 06:00;NOT_A_QUALITY"
        ), StandardCharsets.UTF_8);

        String out = captureStdout(() -> {
            try {
                SleepingSessionsLoader.readLogToSleepingSessions(file.toString());
            } catch (Exception e) {
                fail("Исключение не ожидается при некорректной строке: " + e);
            }
        });

        List<SleepingSession> sessions = SleepingSessionsLoader.readLogToSleepingSessions(file.toString());
        assertEquals(1, sessions.size());
        assertNull(sessions.get(0));
        assertTrue(out.contains("Пропущена некорректная строка:"), "Ожидается сообщение о пропуске строки");
    }

    @Test
    @DisplayName("строка без разделителей не падает, печатает сообщение и возвращает null в списке")
    public void malformedLinePrintsMessageAndAddsNull() throws Exception {
        Path file = tempDir.resolve("malformed.txt");
        Files.write(file, List.of(
                "01.01.26 22:00"
        ), StandardCharsets.UTF_8);

        String out = captureStdout(() -> {
            try {
                SleepingSessionsLoader.readLogToSleepingSessions(file.toString());
            } catch (Exception e) {
                fail("Исключение не ожидается при некорректной строке: " + e);
            }
        });

        List<SleepingSession> sessions = SleepingSessionsLoader.readLogToSleepingSessions(file.toString());
        assertEquals(1, sessions.size());
        assertNull(sessions.get(0));
        assertTrue(out.contains("Пропущена некорректная строка:"), "Ожидается сообщение о пропуске строки");
    }

    @Test
    @DisplayName("смешанный файл: корректные строки + некорректные строки")
    public void mixedFileContainsNullsForBadLines() throws Exception {
        Path file = tempDir.resolve("mixed.txt");
        Files.write(file, List.of(
                "01.01.26 22:00;02.01.26 06:00;GOOD",
                "bad_line",
                "02.01.26 23:00;03.01.26 07:00;NORMAL",
                "01.01.26 22:00;02.01.26 06:00;NOT_A_QUALITY"
        ), StandardCharsets.UTF_8);

        List<SleepingSession> sessions = SleepingSessionsLoader.readLogToSleepingSessions(file.toString());

        assertEquals(4, sessions.size());
        assertEquals(2, sessions.stream().filter(Objects::isNull).count());
        assertEquals(2, sessions.stream().filter(Objects::nonNull).count());
    }

    private String captureStdout(Runnable action) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            action.run();
        } finally {
            System.setOut(oldOut);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
