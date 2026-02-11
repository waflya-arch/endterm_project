package com.university.election.patterns.singleton;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {
    private static AuditLogger instance;
    private PrintWriter logWriter;
    private final DateTimeFormatter formatter;
    private final String logFilePath = "logs/election-audit.log";

    // Private constructor
    private AuditLogger() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        initializeLogFile();
    }

    public static AuditLogger getInstance() {
        if (instance == null) {
            synchronized (AuditLogger.class) {
                if (instance == null) {
                    instance = new AuditLogger();
                }
            }
        }
        return instance;
    }

    private void initializeLogFile() {
        try {
            logWriter = new PrintWriter(new FileWriter(logFilePath, true), true);
        } catch (IOException e) {
            System.err.println("Failed to initialize audit log file: " + e.getMessage());
        }
    }

    public void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);

        // Console output
        System.out.println(logMessage);

        // File output
        if (logWriter != null) {
            logWriter.println(logMessage);
        }
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void debug(String message) {
        log("DEBUG", message);
    }

    public void warn(String message) {
        log("WARN", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    public void error(String message, Exception e) {
        log("ERROR", message + " - " + e.getMessage());
        if (logWriter != null) {
            e.printStackTrace(logWriter);
        }
    }

    public void logElectionAction(String action, String details) {
        log("AUDIT", String.format("[%s] %s", action, details));
    }

    public void logVote(Integer studentId, Integer candidateId, Integer electionId) {
        logElectionAction("VOTE_CAST",
                String.format("Student ID=%d voted for Candidate ID=%d in Election ID=%d",
                        studentId, candidateId, electionId));
    }

    public void logCandidateRegistration(String candidateName, Integer electionId) {
        logElectionAction("CANDIDATE_REGISTERED",
                String.format("Candidate '%s' registered for Election ID=%d",
                        candidateName, electionId));
    }

    public void close() {
        if (logWriter != null) {
            logWriter.close();
        }
    }
}