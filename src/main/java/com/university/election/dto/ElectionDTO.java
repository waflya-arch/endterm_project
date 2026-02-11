package com.university.election.dto;

import java.time.LocalDate;

public class ElectionDTO {
    private Integer id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String status;
    private Integer totalVotes;
    private Integer totalCandidates;
    private String location;
    private String electionType;
    private boolean votingOpen;
    private boolean isActive;

    // Constructors
    public ElectionDTO() {}

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getTotalVotes() { return totalVotes; }
    public void setTotalVotes(Integer totalVotes) { this.totalVotes = totalVotes; }

    public Integer getTotalCandidates() { return totalCandidates; }
    public void setTotalCandidates(Integer totalCandidates) { this.totalCandidates = totalCandidates; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getElectionType() { return electionType; }
    public void setElectionType(String electionType) { this.electionType = electionType; }

    public boolean isVotingOpen() { return votingOpen; }
    public void setVotingOpen(boolean votingOpen) { this.votingOpen = votingOpen; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}