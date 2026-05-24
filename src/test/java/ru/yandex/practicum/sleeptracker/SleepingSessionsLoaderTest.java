package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SleepingSessionsLoaderTest {

    @TempDir
    Path tempDir;

    @Test
    public void testReadValidFile() throws IOException {
        Path file = tempDir.resolve("valid_log.txt");
        List<String> lines = List.of(
                "01.01.26 22:00;02.01.26 06:00;GOOD",
                "02.01.26 23:00;03.01.26 07:00;NORMAL"
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        List<SleepingSession> sessions = SleepingSessionsLoader.readLogToSleepingSessions(file.toString());

        assertEquals(2, sessions.size());
        SleepingSession first = sessions.get(0);
        assertEquals(SleepQuality.GOOD, first.sleepQuality);
        assertEquals(480L, first.duration.toMinutes());

        SleepingSession second = sessions.get(1);
        assertEquals(SleepQuality.NORMAL, second.sleepQuality);
        assertEquals(480L, second.duration.toMinutes());
    }

    @Test
    public void testIgnoresBlankLines() throws IOException {
        Path file = tempDir.resolve("with_blank_lines.txt");
        List<String> lines = List.of(
                "",
                "01.01.26 22:00;02.01.26 06:00;GOOD",
                "   ",
                "02.01.26 23:00;03.01.26 07:00;NORMAL",
                ""
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        List<SleepingSession> sessions = SleepingSessionsLoader.readLogToSleepingSessions(file.toString());

        assertEquals(2, sessions.size());
    }

    @Test
    public void testFileNotFoundThrows() {
        String nonExistent = tempDir.resolve("does_not_exist.txt").toString();
        FileNotFoundException thrown = assertThrows(FileNotFoundException.class,
                () -> SleepingSessionsLoader.readLogToSleepingSessions(nonExistent));
        assertTrue(thrown.getMessage().contains("Не удалось найти файл по указанному пути ->"));
    }

    @Test
    public void testInvalidDateThrows() throws IOException {
        Path file = tempDir.resolve("bad_date.txt");
        List<String> lines = List.of(
                "invalid-date;02.01.26 06:00;GOOD"
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        assertThrows(DateTimeParseException.class,
                () -> SleepingSessionsLoader.readLogToSleepingSessions(file.toString()));
    }

    @Test
    public void testInvalidEnumThrows() throws IOException {
        Path file = tempDir.resolve("bad_enum.txt");
        List<String> lines = List.of(
                "01.01.26 22:00;02.01.26 06:00;NOT_A_QUALITY"
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        assertThrows(IllegalArgumentException.class,
                () -> SleepingSessionsLoader.readLogToSleepingSessions(file.toString()));
    }

    @Test
    public void testMalformedLineThrowsArrayIndexOutOfBounds() throws IOException {
        Path file = tempDir.resolve("malformed.txt");
        List<String> lines = List.of(
                "01.01.26 22:00"
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> SleepingSessionsLoader.readLogToSleepingSessions(file.toString()));
    }
}
