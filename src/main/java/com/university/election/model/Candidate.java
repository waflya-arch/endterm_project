package com.university.election.model;

public class Candidate extends BaseEntity {
    private Integer year;
    private String platform;
    private Integer electionId;
    private Integer voteCount;

    public Candidate() {
        super();
        this.voteCount = 0;
    }

    public Candidate(String name, Integer year, String platform) {
        super(name);
        this.year = year;
        this.platform = platform;
        this.voteCount = 0;
    }

    public Candidate(Integer id, String name, Integer year, String platform, Integer electionId, Integer voteCount) {
        super(id, name);
        this.year = year;
        this.platform = platform;
        this.electionId = electionId;
        this.voteCount = voteCount != null ? voteCount : 0;
    }

    @Override
    public String getDescription() {
        return String.format("Year %d candidate running on platform: %s", year, platform);
    }

    @Override
    public boolean isEligible() {
        // Candidates must be in years 2-4
        return year != null && year >= 2 && year <= 4;
    }

    @Override
    public String getEntityType() {
        return "Candidate";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Year: " + year);
        System.out.println("Platform: " + platform);
        System.out.println("Election ID: " + electionId);
        System.out.println("Vote Count: " + voteCount);
        System.out.println("=================================");
    }

    // Getters and Setters
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Integer getElectionId() {
        return electionId;
    }

    public void setElectionId(Integer electionId) {
        this.electionId = electionId;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public void incrementVoteCount() {
        this.voteCount++;
    }
}