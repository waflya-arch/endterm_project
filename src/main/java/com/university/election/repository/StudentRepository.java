package com.university.election.repository;

import com.university.election.exception.DatabaseOperationException;
import com.university.election.exception.DuplicateResourceException;
import com.university.election.exception.ResourceNotFoundException;
import com.university.election.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
 * Student Repository Implementation
 */
@Repository
public class StudentRepository implements CrudRepository<Student, Integer> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Student> rowMapper = (rs, rowNum) -> new Student(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("student_id"),
            rs.getString("faculty"),
            rs.getInt("year_of_study"),
            rs.getBoolean("has_voted")
    );

    @Override
    public Student save(Student student) {
        String sql = "INSERT INTO students (name, student_id, faculty, year_of_study, has_voted) VALUES (?, ?, ?, ?, ?)";

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, student.getName());
                ps.setString(2, student.getStudentId());
                ps.setString(3, student.getFaculty());
                ps.setInt(4, student.getYearOfStudy());
                ps.setBoolean(5, student.getHasVoted());
                return ps;
            }, keyHolder);

            student.setId(keyHolder.getKey().intValue());
            return student;
        } catch (DuplicateKeyException e) {
            throw new DuplicateResourceException("Student ID already exists: " + student.getStudentId());
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to save student: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Student> findById(Integer id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try {
            Student student = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(student);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Student> findAll() {
        String sql = "SELECT * FROM students ORDER BY id";
        try {
            return jdbcTemplate.query(sql, rowMapper);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch students: " + e.getMessage(), e);
        }
    }

    public Optional<Student> findByStudentId(String studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try {
            Student student = jdbcTemplate.queryForObject(sql, rowMapper, studentId);
            return Optional.ofNullable(student);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Student> findByVotingStatus(Boolean hasVoted) {
        String sql = "SELECT * FROM students WHERE has_voted = ? ORDER BY id";
        try {
            return jdbcTemplate.query(sql, rowMapper, hasVoted);
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to fetch students: " + e.getMessage(), e);
        }
    }

    @Override
    public Student update(Integer id, Student student) {
        String sql = "UPDATE students SET name = ?, student_id = ?, faculty = ?, year_of_study = ?, has_voted = ? WHERE id = ?";

        try {
            int rows = jdbcTemplate.update(sql,
                    student.getName(),
                    student.getStudentId(),
                    student.getFaculty(),
                    student.getYearOfStudy(),
                    student.getHasVoted(),
                    id
            );

            if (rows == 0) {
                throw new ResourceNotFoundException("Student not found with id: " + id);
            }

            student.setId(id);
            return student;
        } catch (DuplicateKeyException e) {
            throw new DuplicateResourceException("Student ID already exists: " + student.getStudentId());
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to update student: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try {
            int rows = jdbcTemplate.update(sql, id);
            if (rows == 0) {
                throw new ResourceNotFoundException("Student not found with id: " + id);
            }
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to delete student: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM students WHERE id = ?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM students";
        try {
            Long count = jdbcTemplate.queryForObject(sql, Long.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}