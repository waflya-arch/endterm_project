package com.university.election.model;

public class Student extends BaseEntity {
    private String studentId;
    private Integer year;
    private String major;
    private boolean hasVoted;

    public Student() {
        super();
        this.hasVoted = false;
    }

    public Student(String name, String studentId, Integer year, String major) {
        super(name);
        this.studentId = studentId;
        this.year = year;
        this.major = major;
        this.hasVoted = false;
    }

    public Student(Integer id, String name, String studentId, Integer year, String major, boolean hasVoted) {
        super(id, name);
        this.studentId = studentId;
        this.year = year;
        this.major = major;
        this.hasVoted = hasVoted;
    }

    @Override
    public String getDescription() {
        return String.format("Year %d %s student - %s", year, major, (hasVoted ? "Has voted" : "Has not voted"));
    }

    @Override
    public boolean isEligible() {
        // Students in years 1-4 are eligible to vote
        return year != null && year >= 1 && year <= 4;
    }

    @Override
    public String getEntityType() {
        return "Student";
    }

    public boolean canVote() {
        return isEligible() && !hasVoted;
    }

    public void recordVote() {
        this.hasVoted = true;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Student ID: " + studentId);
        System.out.println("Year: " + year);
        System.out.println("Major: " + major);
        System.out.println("Has Voted: " + (hasVoted ? "Yes" : "No"));
        System.out.println("Can Vote: " + (canVote() ? "Yes" : "No"));
        System.out.println("=================================");
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
}