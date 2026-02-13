package com.university.election.patterns.builder;

import com.university.election.model.Election;

import java.time.LocalDate;

/**
 * Builder Pattern: Election Builder
 * Constructs complex Election objects with fluent API
 */
public class ElectionBuilder {
    private Integer id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String academicYear;

    public ElectionBuilder() {
    }

    /**
     * Fluent methods for building Election
     */
    public ElectionBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public ElectionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ElectionBuilder withStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public ElectionBuilder withEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public ElectionBuilder withAcademicYear(String academicYear) {
        this.academicYear = academicYear;
        return this;
    }

    /**
     * Convenience method to set both dates
     */
    public ElectionBuilder withDateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    /**
     * Build the final Election object
     */
    public Election build() {
        // Validation
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalStateException("Election name is required");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalStateException("Start and end dates are required");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalStateException("Start date must be before end date");
        }

        return new Election(id, name, startDate, endDate, academicYear);
    }

    /**
     * Static factory method for builder
     */
    public static ElectionBuilder builder() {
        return new ElectionBuilder();
    }

    /**
     * Demonstrate builder usage
     */
    public static void demonstrateBuilder() {
        System.out.println("=== Builder Pattern Demonstration ===");

        Election election = ElectionBuilder.builder()
                .withName("University President Election 2026")
                .withDateRange(LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 19))
                .withAcademicYear("2025-2026")
                .build();

        System.out.println("Built election: " + election);
        System.out.println("Period: " + election.getStartDate() + " to " + election.getEndDate());
    }
}