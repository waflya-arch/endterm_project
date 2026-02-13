package com.university.election.service;

import com.university.election.model.Election;

import java.util.List;

/**
 * Election Service Interface
 * Demonstrates Dependency Inversion Principle (DIP)
 */
public interface ElectionService {
    Election createElection(Election election);
    Election getElectionById(Integer id);
    List<Election> getAllElections();
    Election updateElection(Integer id, Election election);
    void deleteElection(Integer id);
    long countElections();
}