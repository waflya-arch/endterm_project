package com.university.election.service;

import com.university.election.exception.InvalidInputException;
import com.university.election.exception.ResourceNotFoundException;
import com.university.election.model.Election;
import com.university.election.patterns.singleton.CacheManager;
import com.university.election.repository.ElectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Election Service Implementation with Caching
 * - Caches getAllElections() results
 * - Caches getElectionById() results
 * - Invalidates cache on create/update/delete
 * - Follows SOLID principles
 * - Maintains layered architecture
 */
@Service
public class ElectionServiceImpl implements ElectionService {

    private final ElectionRepository repository;
    private final CacheManager cacheManager;

    // Cache keys
    private static final String CACHE_KEY_ALL_ELECTIONS = "elections:all";
    private static final String CACHE_KEY_ELECTION_PREFIX = "election:";

    @Autowired
    public ElectionServiceImpl(ElectionRepository repository, CacheManager cacheManager) {
        this.repository = repository;
        this.cacheManager = cacheManager;
    }

    @Override
    public Election createElection(Election election) {
        validateElection(election);
        Election created = repository.save(election);

        // Invalidate cache after create
        cacheManager.invalidatePattern("elections");

        return created;
    }

    @Override
    public Election getElectionById(Integer id) {
        String cacheKey = CACHE_KEY_ELECTION_PREFIX + id;

        // Try to get from cache first
        Optional<Object> cached = cacheManager.get(cacheKey);
        if (cached.isPresent()) {
            return (Election) cached.get();
        }

        // Cache miss - get from database
        Election election = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found with id: " + id));

        // Put in cache for next time
        cacheManager.put(cacheKey, election);

        return election;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Election> getAllElections() {
        // Try to get from cache first
        Optional<Object> cached = cacheManager.get(CACHE_KEY_ALL_ELECTIONS);
        if (cached.isPresent()) {
            return (List<Election>) cached.get();
        }

        // Cache miss - query database
        List<Election> elections = repository.findAll();

        // Put in cache for next time
        cacheManager.put(CACHE_KEY_ALL_ELECTIONS, elections);

        return elections;
    }

    @Override
    public Election updateElection(Integer id, Election election) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Election not found with id: " + id);
        }
        validateElection(election);

        Election updated = repository.update(id, election);

        // Invalidate cache after update
        cacheManager.invalidate(CACHE_KEY_ELECTION_PREFIX + id);
        cacheManager.invalidate(CACHE_KEY_ALL_ELECTIONS);

        return updated;
    }

    @Override
    public void deleteElection(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Election not found with id: " + id);
        }

        repository.deleteById(id);

        // Invalidate cache after delete
        cacheManager.invalidate(CACHE_KEY_ELECTION_PREFIX + id);
        cacheManager.invalidate(CACHE_KEY_ALL_ELECTIONS);
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