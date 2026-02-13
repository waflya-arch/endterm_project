package com.university.election.service;

import com.university.election.model.Candidate;

import java.util.List;

/**
 * Candidate Service Interface
 */
public interface CandidateService {
    Candidate createCandidate(Candidate candidate);
    Candidate getCandidateById(Integer id);
    List<Candidate> getAllCandidates();
    List<Candidate> getCandidatesByElectionId(Integer electionId);
    Candidate updateCandidate(Integer id, Candidate candidate);
    void deleteCandidate(Integer id);
    long countCandidates();
}