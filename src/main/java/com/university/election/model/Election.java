package com.university.election.model;

import java.time.LocalDate;


public class Election {
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

    // Private constructor - only Builder can create instances
    private Election(ElectionBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.description = builder.description;
        this.status = builder.status;
        this.totalVotes = builder.totalVotes;
        this.totalCandidates = builder.totalCandidates;
        this.location = builder.location;
        this.electionType = builder.electionType;
        this.votingOpen = builder.votingOpen;
    }

    // Getters
    public Integer getId() { return id; }
    public String getName() { return name; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public Integer getTotalVotes() { return totalVotes; }
    public Integer getTotalCandidates() { return totalCandidates; }
    public String getLocation() { return location; }
    public String getElectionType() { return electionType; }
    public boolean isVotingOpen() { return votingOpen; }
    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return votingOpen &&
                !now.isBefore(startDate) &&
                !now.isAfter(endDate) &&
                "ACTIVE".equals(status);
    }

    @Override
    public String toString() {
        return String.format("Election[id=%d, name='%s', status=%s, votes=%d, candidates=%d]",
                id, name, status, totalVotes, totalCandidates);
    }

    public static class ElectionBuilder {
        // Required parameters
        private final String name;
        private final LocalDate startDate;
        private final LocalDate endDate;

        // Optional parameters with defaults
        private Integer id;
        private String description = "";
        private String status = "SCHEDULED";
        private Integer totalVotes = 0;
        private Integer totalCandidates = 0;
        private String location = "Main Campus";
        private String electionType = "PRESIDENT";
        private boolean votingOpen = false;

        public ElectionBuilder(String name, LocalDate startDate, LocalDate endDate) {
            this.name = name;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        // Fluent methods for optional parameters
        public ElectionBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public ElectionBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ElectionBuilder status(String status) {
            this.status = status;
            return this;
        }

        public ElectionBuilder totalVotes(Integer totalVotes) {
            this.totalVotes = totalVotes;
            return this;
        }

        public ElectionBuilder totalCandidates(Integer totalCandidates) {
            this.totalCandidates = totalCandidates;
            return this;
        }

        public ElectionBuilder location(String location) {
            this.location = location;
            return this;
        }

        public ElectionBuilder electionType(String electionType) {
            this.electionType = electionType;
            return this;
        }

        public ElectionBuilder votingOpen(boolean votingOpen) {
            this.votingOpen = votingOpen;
            return this;
        }

        public Election build() {
            // Validation
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalStateException("Election name cannot be empty");
            }

            if (startDate == null || endDate == null) {
                throw new IllegalStateException("Start date and end date are required");
            }

            if (endDate.isBefore(startDate)) {
                throw new IllegalStateException("End date cannot be before start date");
            }

            if (totalVotes < 0) {
                throw new IllegalStateException("Total votes cannot be negative");
            }

            if (totalCandidates < 0) {
                throw new IllegalStateException("Total candidates cannot be negative");
            }

            return new Election(this);
        }
    }
}