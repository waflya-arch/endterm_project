package com.university.election.patterns.singleton;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton Pattern: Application Logger
 * Single shared logging service across the application
 */
@Component
public class AppLogger {

    private static AppLogger instance;
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AppLogger() {
        if (instance != null) {
            throw new IllegalStateException("AppLogger instance already exists!");
        }
        instance = this;
    }

    public static AppLogger getInstance() {
        if (instance == null) {
            synchronized (AppLogger.class) {
                if (instance == null) {
                    instance = new AppLogger();
                }
            }
        }
        return instance;
    }

    public void info(String message) {
        System.out.println("[INFO] " + getCurrentTime() + " - " + message);
    }

    public void debug(String message) {
        System.out.println("[DEBUG] " + getCurrentTime() + " - " + message);
    }

    public void error(String message) {
        System.err.println("[ERROR] " + getCurrentTime() + " - " + message);
    }

    private String getCurrentTime() {
        return LocalDateTime.now().format(formatter);
    }
}