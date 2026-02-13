package com.university.election.service;

import com.university.election.exception.InvalidInputException;
import com.university.election.exception.ResourceNotFoundException;
import com.university.election.model.Election;
import com.university.election.repository.ElectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Election Service Implementation
 * Implements business logic and validation
 * Demonstrates Single Responsibility Principle (SRP)
 */
@Service
public class ElectionServiceImpl implements ElectionService {

    private final ElectionRepository repository;

    @Autowired
    public ElectionServiceImpl(ElectionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Election createElection(Election election) {
        validateElection(election);
        return repository.save(election);
    }

    @Override
    public Election getElectionById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found with id: " + id));
    }

    @Override
    public List<Election> getAllElections() {
        return repository.findAll();
    }

    @Override
    public Election updateElection(Integer id, Election election) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Election not found with id: " + id);
        }
        validateElection(election);
        return repository.update(id, election);
    }

    @Override
    public void deleteElection(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Election not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public long countElections() {
        return repository.count();
    }

    /**
     * Validate election data
     */
    private void validateElection(Election election) {
        if (election.getName() == null || election.getName().trim().isEmpty()) {
            throw new InvalidInputException("Election name cannot be empty");
        }

        if (election.getStartDate() == null) {
            throw new InvalidInputException("Start date cannot be null");
        }

        if (election.getEndDate() == null) {
            throw new InvalidInputException("End date cannot be null");
        }

        if (election.getStartDate().isAfter(election.getEndDate())) {
            throw new InvalidInputException("Start date must be before end date");
        }

        if (election.getAcademicYear() == null || election.getAcademicYear().trim().isEmpty()) {
            throw new InvalidInputException("Academic year cannot be empty");
        }
    }
}