package com.university.election.model;

import com.university.election.model.interfaces.Validatable;

/**
 * Candidate entity extending BaseEntity
 * Demonstrates Inheritance, Polymorphism, and Composition
 * A Candidate MUST have an Election (composition)
 */
public class Candidate extends BaseEntity implements Validatable {
    private String faculty;
    private Integer yearOfStudy;
    private String campaign;
    private Election election; // Composition: Candidate contains Election

    public Candidate() {
        super();
    }

    public Candidate(Integer id, String name, String faculty, Integer yearOfStudy,
                     String campaign, Election election) {
        super(id, name);
        this.faculty = faculty;
        this.yearOfStudy = yearOfStudy;
        this.campaign = campaign;
        this.election = election;
    }

    // Abstract method implementation from BaseEntity
    @Override
    public String getDescription() {
        return "Candidate running for University President in " +
                (election != null ? election.getName() : "Unknown Election") +
                ". Campaign: " + (campaign != null ? campaign : "No campaign message");
    }

    // Abstract method implementation from BaseEntity
    @Override
    public boolean isEligible() {
        // Candidates must be in year 2, 3, or 4
        return yearOfStudy != null && yearOfStudy >= 2 && yearOfStudy <= 4;
    }

    // Validatable interface implementation
    @Override
    public boolean validate() {
        return Validatable.isNotEmpty(name) &&
                Validatable.isNotEmpty(faculty) &&
                yearOfStudy != null &&
                isEligible() &&
                election != null;
    }

    // Getters and Setters
    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public Integer getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(Integer yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    @Override
    public String toString() {
        return "Candidate{id=" + id + ", name='" + name + "', faculty='" + faculty +
                "', year=" + yearOfStudy + ", electionId=" +
                (election != null ? election.getId() : "null") + "}";
    }
}