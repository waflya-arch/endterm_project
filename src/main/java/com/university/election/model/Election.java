package com.university.election.model;

import java.time.LocalDate;

/**
 * Election entity representing a university president election
 * Used in composition with Candidate
 */
public class Election {
    private Integer id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String academicYear;

    public Election() {
    }

    public Election(Integer id, String name, LocalDate startDate, LocalDate endDate, String academicYear) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.academicYear = academicYear;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    @Override
    public String toString() {
        return "Election{id=" + id + ", name='" + name + "', academicYear='" + academicYear + "'}";
    }
}