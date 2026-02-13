package com.university.election.repository;

import com.university.election.exception.DatabaseOperationException;
import com.university.election.exception.ResourceNotFoundException;
import com.university.election.model.Election;
import com.university.election.patterns.builder.ElectionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Election Repository Implementation
 * Implements generic CrudRepository interface
 * Uses JDBC with Spring JdbcTemplate
 */
@Repository
public class ElectionRepository implements CrudRepository<Election, Integer> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Election> rowMapper = (rs, rowNum) ->
            ElectionBuilder.builder()
                    .withId(rs.getInt("id"))
                    .withName(rs.getString("name"))
                    .withStartDate(rs.getDate("start_date").toLocalDate())
                    .withEndDate(rs.getDate("end_date").toLocalDate())
                    .withAcademicYear(rs.getString("academic_year"))
                    .build();

    @Override
    public Election save(Election election) {
        String sql = "INSERT INTO elections (name, start_date, end_date, academic_year) VALUES (?, ?, ?, ?)";

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, election.getName());
                ps.setDate(2, Date.valueOf(election.getStartDate()));
                ps.setDate(3, Date.valueOf(election.getEndDate()));
                ps.setString(4, election.getAcademicYear());
                return ps;
            }, keyHolder);

            election.setId(keyHolder.getKey().intValue());
            return election;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to save election: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Election> findById(Integer id) {
        String sql = "SELECT * FROM elections WHERE id = ?";
        try {
            Election election = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(election);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Election> findAll() {
        String sql = "SELECT * FROM elections ORDER BY start_date DESC";
        try {
            return jdbcTemplate.query(sql, rowMapper);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch elections: " + e.getMessage(), e);
        }
    }

    @Override
    public Election update(Integer id, Election election) {
        String sql = "UPDATE elections SET name = ?, start_date = ?, end_date = ?, academic_year = ? WHERE id = ?";

        try {
            int rows = jdbcTemplate.update(sql,
                    election.getName(),
                    Date.valueOf(election.getStartDate()),
                    Date.valueOf(election.getEndDate()),
                    election.getAcademicYear(),
                    id
            );

            if (rows == 0) {
                throw new ResourceNotFoundException("Election not found with id: " + id);
            }

            election.setId(id);
            return election;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to update election: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM elections WHERE id = ?";
        try {
            int rows = jdbcTemplate.update(sql, id);
            if (rows == 0) {
                throw new ResourceNotFoundException("Election not found with id: " + id);
            }
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to delete election: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM elections WHERE id = ?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM elections";
        try {
            Long count = jdbcTemplate.queryForObject(sql, Long.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}