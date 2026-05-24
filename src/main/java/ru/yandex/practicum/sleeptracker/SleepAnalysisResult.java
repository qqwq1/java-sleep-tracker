package ru.yandex.practicum.sleeptracker;

public class SleepAnalysisResult {
    protected final String description;
    protected final String result;

    public SleepAnalysisResult(String description, String result) {
        this.description = description;
        this.result = result;
    }
}
