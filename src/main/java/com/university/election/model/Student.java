package com.university.election.model;

import com.university.election.model.interfaces.Validatable;
import com.university.election.model.interfaces.Votable;

/**
 * Student entity extending BaseEntity
 * Demonstrates Inheritance, Polymorphism, and Multiple Interfaces
 */
public class Student extends BaseEntity implements Validatable, Votable {
    private String studentId;
    private String faculty;
    private Integer yearOfStudy;
    private Boolean hasVoted;

    public Student() {
        super();
        this.hasVoted = false;
    }

    public Student(Integer id, String name, String studentId, String faculty,
                   Integer yearOfStudy, Boolean hasVoted) {
        super(id, name);
        this.studentId = studentId;
        this.faculty = faculty;
        this.yearOfStudy = yearOfStudy;
        this.hasVoted = hasVoted != null ? hasVoted : false;
    }

    // Abstract method implementation from BaseEntity
    @Override
    public String getDescription() {
        return "Student in " + faculty + ", Year " + yearOfStudy +
                ". Voting Status: " + (hasVoted ? "Already voted" : "Not yet voted");
    }

    // Abstract method implementation from BaseEntity
    @Override
    public boolean isEligible() {
        // All students (year 1-4) are eligible
        return yearOfStudy != null && yearOfStudy >= 1 && yearOfStudy <= 4;
    }

    // Validatable interface implementation
    @Override
    public boolean validate() {
        return Validatable.isNotEmpty(name) &&
                Validatable.isNotEmpty(studentId) &&
                Validatable.isNotEmpty(faculty) &&
                yearOfStudy != null &&
                isEligible();
    }

    // Votable interface implementation
    @Override
    public void vote() {
        if (canVote()) {
            this.hasVoted = true;
            System.out.println(name + " has successfully voted!");
        } else {
            System.out.println(name + " cannot vote (already voted or not eligible)");
        }
    }

    @Override
    public boolean canVote() {
        return isEligible() && !hasVoted;
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

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

    public Boolean getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(Boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', studentId='" + studentId +
                "', faculty='" + faculty + "', year=" + yearOfStudy + ", voted=" + hasVoted + "}";
    }
}