package com.university.election.model.interfaces;

/**
 * Interface for entities that can vote
 * Demonstrates Interface Segregation Principle (ISP)
 */
public interface Votable {
    /**
     * Cast a vote
     */
    void vote();

    /**
     * Check if entity can vote
     * @return true if can vote, false otherwise
     */
    boolean canVote();

    /**
     * Default method to check voting status
     */
    default String getVotingStatus() {
        return canVote() ? "Eligible to vote" : "Not eligible to vote";
    }
}