package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SleepTrackerAppTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("валидный файл печатает 7 строк результатов")
    public void printsSixResultLinesForValidFile() throws Exception {
        Path file = tempDir.resolve("log.txt");
        Files.write(file, List.of(
                "01.01.25 23:00;02.01.25 07:00;GOOD",
                "02.01.25 12:00;02.01.25 13:00;NORMAL"
        ), StandardCharsets.UTF_8);

        String output = runMainAndCaptureOutput(file.toString());
        String[] lines = output.strip().split("\\R");

        assertEquals(7, lines.length);
        for (String line : lines) {
            assertTrue(line.contains(" -> "));
            assertFalse(line.startsWith(" -> "));
            assertFalse(line.endsWith(" -> "));
        }
    }

    @Test
    @DisplayName("вывод содержит описания всех функций")
    public void printsAllFunctionDescriptions() throws Exception {
        Path file = tempDir.resolve("log2.txt");
        Files.write(file, List.of(
                "01.01.25 23:15;02.01.25 07:30;GOOD",
                "02.01.25 23:50;03.01.25 06:40;NORMAL",
                "03.01.25 14:10;03.01.25 15:00;NORMAL"
        ), StandardCharsets.UTF_8);

        String output = runMainAndCaptureOutput(file.toString());

        assertAll(
                () -> assertTrue(output.contains("Количество сессий сна за данный период")),
                () -> assertTrue(output.contains("Минимальная продолжительность сессии сна за данный период")),
                () -> assertTrue(output.contains("Максимальная продолжительность сессии сна за данный период")),
                () -> assertTrue(output.contains("Средняя продолжительность сна за данный период")),
                () -> assertTrue(output.contains("Количество бессонных ночей за данный период")),
                () -> assertTrue(output.contains("Тип пользователя за данный период"))
        );
    }

    @Test
    @DisplayName("нет файла: печатается сообщение об ошибке")
    public void printsErrorWhenFileMissing() {
        Path missing = tempDir.resolve("no_such_file.txt");

        String output = runMainAndCaptureOutput(missing.toString());

        assertTrue(output.contains("Не удалось найти файл по указанному пути"),
                "Ожидается сообщение о том, что файл не найден");
    }

    @Test
    @DisplayName("корректные значения: счетчик, минимум, максимум, среднее")
    public void printsExpectedNumbersForSimpleLog() throws Exception {
        Path file = tempDir.resolve("simple_log.txt");
        Files.write(file, List.of(
                "01.01.25 00:00;01.01.25 01:00;GOOD",
                "02.01.25 00:00;02.01.25 02:00;NORMAL",
                "03.01.25 00:00;03.01.25 00:30;BAD"
        ), StandardCharsets.UTF_8);

        String output = runMainAndCaptureOutput(file.toString());

        assertTrue(output.contains("Количество сессий сна за данный период -> 3"));
        assertTrue(output.contains("Минимальная продолжительность сессии сна за данный период -> 30"));
        assertTrue(output.contains("Максимальная продолжительность сессии сна за данный период -> 120"));
        assertTrue(output.contains("Средняя продолжительность сна за данный период -> 70.0"));
    }

    @Test
    @DisplayName("пустой лог: минимум/максимум/среднее = 0")
    public void emptyLogProducesDefaults() throws Exception {
        Path file = tempDir.resolve("empty_log.txt");
        Files.write(file, List.of(
                "",
                "   ",
                "\t"
        ), StandardCharsets.UTF_8);

        String output = runMainAndCaptureOutput(file.toString());
        String[] lines = output.strip().split("\\R");

        assertEquals(7, lines.length);
        assertTrue(output.contains("Количество сессий сна за данный период -> 0"));
        assertTrue(output.contains("Минимальная продолжительность сессии сна за данный период -> 0"));
        assertTrue(output.contains("Максимальная продолжительность сессии сна за данный период -> 0"));
        assertTrue(output.contains("Средняя продолжительность сна за данный период -> 0.0"));
    }

    private String runMainAndCaptureOutput(String path) {
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(outBuf, true, StandardCharsets.UTF_8));
        try {
            SleepTrackerApp.main(new String[]{path});
        } finally {
            System.setOut(oldOut);
        }
        return outBuf.toString(StandardCharsets.UTF_8);
    }
}
