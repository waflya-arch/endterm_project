package com.university.election.repository;

import com.university.election.exception.DatabaseOperationException;
import com.university.election.exception.ResourceNotFoundException;
import com.university.election.model.Candidate;
import com.university.election.model.Election;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Candidate Repository Implementation
 */
@Repository
public class CandidateRepository implements CrudRepository<Candidate, Integer> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ElectionRepository electionRepository;

    private final RowMapper<Candidate> rowMapper = (rs, rowNum) -> {
        Candidate candidate = new Candidate();
        candidate.setId(rs.getInt("id"));
        candidate.setName(rs.getString("name"));
        candidate.setFaculty(rs.getString("faculty"));
        candidate.setYearOfStudy(rs.getInt("year_of_study"));
        candidate.setCampaign(rs.getString("campaign"));

        // Load election (composition)
        Integer electionId = rs.getInt("election_id");
        Election election = electionRepository.findById(electionId).orElse(null);
        candidate.setElection(election);

        return candidate;
    };

    @Override
    public Candidate save(Candidate candidate) {
        String sql = "INSERT INTO candidates (name, faculty, year_of_study, campaign, election_id) VALUES (?, ?, ?, ?, ?)";

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, candidate.getName());
                ps.setString(2, candidate.getFaculty());
                ps.setInt(3, candidate.getYearOfStudy());
                ps.setString(4, candidate.getCampaign());
                ps.setInt(5, candidate.getElection().getId());
                return ps;
            }, keyHolder);

            candidate.setId(keyHolder.getKey().intValue());
            return candidate;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to save candidate: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Candidate> findById(Integer id) {
        String sql = "SELECT * FROM candidates WHERE id = ?";
        try {
            Candidate candidate = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(candidate);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Candidate> findAll() {
        String sql = "SELECT * FROM candidates ORDER BY id";
        try {
            return jdbcTemplate.query(sql, rowMapper);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch candidates: " + e.getMessage(), e);
        }
    }

    public List<Candidate> findByElectionId(Integer electionId) {
        String sql = "SELECT * FROM candidates WHERE election_id = ? ORDER BY id";
        try {
            return jdbcTemplate.query(sql, rowMapper, electionId);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch candidates: " + e.getMessage(), e);
        }
    }

    @Override
    public Candidate update(Integer id, Candidate candidate) {
        String sql = "UPDATE candidates SET name = ?, faculty = ?, year_of_study = ?, campaign = ?, election_id = ? WHERE id = ?";

        try {
            int rows = jdbcTemplate.update(sql,
                    candidate.getName(),
                    candidate.getFaculty(),
                    candidate.getYearOfStudy(),
                    candidate.getCampaign(),
                    candidate.getElection().getId(),
                    id
            );

            if (rows == 0) {
                throw new ResourceNotFoundException("Candidate not found with id: " + id);
            }

            candidate.setId(id);
            return candidate;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to update candidate: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM candidates WHERE id = ?";
        try {
            int rows = jdbcTemplate.update(sql, id);
            if (rows == 0) {
                throw new ResourceNotFoundException("Candidate not found with id: " + id);
            }
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to delete candidate: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM candidates WHERE id = ?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM candidates";
        try {
            Long count = jdbcTemplate.queryForObject(sql, Long.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}