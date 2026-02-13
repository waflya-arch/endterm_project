package com.university.election.patterns.factory;

import com.university.election.model.BaseEntity;
import com.university.election.model.Candidate;
import com.university.election.model.Student;
import com.university.election.model.Election;
import org.springframework.stereotype.Component;

/**
 * Factory Pattern: Entity Factory
 * Creates different types of BaseEntity subclasses
 * Demonstrates polymorphism and Open-Closed Principle
 */
@Component
public class EntityFactory {

    public enum EntityType {
        CANDIDATE,
        STUDENT
    }

    /**
     * Factory method to create entities
     * Returns BaseEntity type, demonstrating polymorphism
     */
    public BaseEntity createEntity(EntityType type, Object... params) {
        switch (type) {
            case CANDIDATE:
                return createCandidate(params);
            case STUDENT:
                return createStudent(params);
            default:
                throw new IllegalArgumentException("Unknown entity type: " + type);
        }
    }

    /**
     * Create a Candidate entity
     */
    private Candidate createCandidate(Object[] params) {
        if (params.length < 6) {
            throw new IllegalArgumentException("Insufficient parameters for Candidate");
        }

        return new Candidate(
                (Integer) params[0],  // id
                (String) params[1],   // name
                (String) params[2],   // faculty
                (Integer) params[3],  // yearOfStudy
                (String) params[4],   // campaign
                (Election) params[5]  // election
        );
    }

    /**
     * Create a Student entity
     */
    private Student createStudent(Object[] params) {
        if (params.length < 6) {
            throw new IllegalArgumentException("Insufficient parameters for Student");
        }

        return new Student(
                (Integer) params[0],  // id
                (String) params[1],   // name
                (String) params[2],   // studentId
                (String) params[3],   // faculty
                (Integer) params[4],  // yearOfStudy
                (Boolean) params[5]   // hasVoted
        );
    }

    /**
     * Demonstrate factory usage
     */
    public void demonstrateFactory() {
        System.out.println("=== Factory Pattern Demonstration ===");

        // Create Election for candidate
        Election election = new Election(1, "Test Election", null, null, "2025-2026");

        // Create entities using factory
        BaseEntity candidate = createEntity(EntityType.CANDIDATE,
                1, "John Doe", "Computer Science", 3, "Better campus!", election);

        BaseEntity student = createEntity(EntityType.STUDENT,
                1, "Jane Smith", "S001", "Software Engineering", 2, false);

        // Polymorphic behavior
        System.out.println("Candidate: " + candidate.getDescription());
        System.out.println("Student: " + student.getDescription());
    }
}