package com.university.election.patterns.factory;

import com.university.election.model.*;
import com.university.election.patterns.singleton.AuditLogger;

public class EntityFactory {
    private static final AuditLogger logger = AuditLogger.getInstance();

    public enum EntityType {
        CANDIDATE,
        STUDENT
    }

    public static BaseEntity createEntity(EntityType type, String name) {
        logger.debug("Creating entity of type: " + type + " with name: " + name);

        BaseEntity entity = switch (type) {
            case CANDIDATE -> {
                Candidate candidate = new Candidate();
                candidate.setName(name);
                candidate.setYear(2); // Default year for candidates
                candidate.setPlatform("Platform to be defined");
                candidate.setVoteCount(0);
                yield candidate;
            }
            case STUDENT -> {
                Student student = new Student();
                student.setName(name);
                student.setStudentId("STU" + System.currentTimeMillis());
                student.setYear(1); // Default year for students
                student.setMajor("Undeclared");
                student.setHasVoted(false);
                yield student;
            }
        };

        logger.info("Entity created: " + entity.getEntityType() + " - " + name);
        return entity;
    }

    public static BaseEntity createCandidate(String name, Integer year, String platform) {
        logger.debug(String.format("Creating Candidate: name=%s, year=%d, platform=%s",
                name, year, platform));

        Candidate candidate = new Candidate(name, year, platform);

        logger.info("Candidate created: " + name);
        return candidate;
    }

    public static BaseEntity createCandidate(String name, Integer year, String platform, Integer electionId) {
        logger.debug(String.format("Creating Candidate: name=%s, year=%d, electionId=%d",
                name, year, electionId));

        Candidate candidate = new Candidate(name, year, platform);
        candidate.setElectionId(electionId);

        logger.logCandidateRegistration(name, electionId);
        return candidate;
    }

    public static BaseEntity createStudent(String name, String studentId, Integer year, String major) {
        logger.debug(String.format("Creating Student: name=%s, studentId=%s, year=%d, major=%s",
                name, studentId, year, major));

        Student student = new Student(name, studentId, year, major);

        logger.info("Student created: " + name + " (" + studentId + ")");
        return student;
    }

    public static BaseEntity createValidatedCandidate(String name, Integer year, String platform) {
        // Validate year (candidates must be in years 2-4)
        if (year < 2 || year > 4) {
            logger.error("Invalid candidate year: " + year);
            throw new IllegalArgumentException("Candidates must be in years 2-4");
        }

        // Validate name
        if (name == null || name.trim().isEmpty()) {
            logger.error("Invalid candidate name");
            throw new IllegalArgumentException("Candidate name cannot be empty");
        }

        // Validate platform
        if (platform == null || platform.trim().isEmpty()) {
            logger.error("Invalid candidate platform");
            throw new IllegalArgumentException("Candidate platform cannot be empty");
        }

        return createCandidate(name, year, platform);
    }

    public static BaseEntity createValidatedStudent(String name, String studentId, Integer year, String major) {
        // Validate year (students in years 1-4)
        if (year < 1 || year > 4) {
            logger.error("Invalid student year: " + year);
            throw new IllegalArgumentException("Students must be in years 1-4");
        }

        // Validate name
        if (name == null || name.trim().isEmpty()) {
            logger.error("Invalid student name");
            throw new IllegalArgumentException("Student name cannot be empty");
        }

        // Validate student ID
        if (studentId == null || studentId.trim().isEmpty()) {
            logger.error("Invalid student ID");
            throw new IllegalArgumentException("Student ID cannot be empty");
        }

        return createStudent(name, studentId, year, major);
    }
}