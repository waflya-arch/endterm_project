package com.university.election.service;

import com.university.election.exception.InvalidInputException;
import com.university.election.exception.ResourceNotFoundException;
import com.university.election.model.Candidate;
import com.university.election.repository.CandidateRepository;
import com.university.election.repository.ElectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Candidate Service Implementation
 * Implements business logic and validation
 */
@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository repository;
    private final ElectionRepository electionRepository;

    @Autowired
    public CandidateServiceImpl(CandidateRepository repository, ElectionRepository electionRepository) {
        this.repository = repository;
        this.electionRepository = electionRepository;
    }

    @Override
    public Candidate createCandidate(Candidate candidate) {
        validateCandidate(candidate);
        return repository.save(candidate);
    }

    @Override
    public Candidate getCandidateById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
    }

    @Override
    public List<Candidate> getAllCandidates() {
        return repository.findAll();
    }

    @Override
    public List<Candidate> getCandidatesByElectionId(Integer electionId) {
        if (!electionRepository.existsById(electionId)) {
            throw new ResourceNotFoundException("Election not found with id: " + electionId);
        }
        return repository.findByElectionId(electionId);
    }

    @Override
    public Candidate updateCandidate(Integer id, Candidate candidate) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Candidate not found with id: " + id);
        }
        validateCandidate(candidate);
        return repository.update(id, candidate);
    }

    @Override
    public void deleteCandidate(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Candidate not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public long countCandidates() {
        return repository.count();
    }

    /**
     * Validate candidate data
     */
    private void validateCandidate(Candidate candidate) {
        if (!candidate.validate()) {
            throw new InvalidInputException("Invalid candidate data: " + candidate.getValidationMessage());
        }

        if (!candidate.isEligible()) {
            throw new InvalidInputException("Candidate must be in year 2, 3, or 4. Current year: " +
                    candidate.getYearOfStudy());
        }

        if (candidate.getElection() == null || candidate.getElection().getId() == null) {
            throw new InvalidInputException("Candidate must be associated with an election");
        }

        if (!electionRepository.existsById(candidate.getElection().getId())) {
            throw new ResourceNotFoundException("Election not found with id: " + candidate.getElection().getId());
        }
    }
}