package ru.yandex.practicum.sleeptracker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SleepingSessionsLoader {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static List<SleepingSession> readLogToSleepingSessions(String pathToLogFile) throws IOException {
        Path path = Path.of(pathToLogFile);
        //Читаем файл по пути path, переданного в качестве аргумента командной строки метода main
        //файл должен содержать строки формата
        //01.10.25 22:15;02.10.25 08:00;GOOD
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            return reader.lines()
                    .filter(line -> !line.isBlank())
                    .map(SleepingSessionsLoader::parseLine)
                    .toList();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Не удалось найти файл по указанному пути ->" + path);
        } catch (IOException e) {
            throw new IOException("При чтении файла произошла ошибка, проверьте его целостность");
        }
    }

    private static SleepingSession parseLine(String line) {
        try {
            String[] content = line.trim().split(";");
            LocalDateTime start = LocalDateTime.parse(content[0], formatter);
            LocalDateTime finish = LocalDateTime.parse(content[1], formatter);
            SleepQuality sleepQuality = SleepQuality.valueOf(content[2]);
            return new SleepingSession(start, finish, sleepQuality);
        } catch (Exception e) {
            System.out.println("Пропущена некорректная строка: " + line);
            return null;
        }
    }
}
