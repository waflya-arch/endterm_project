package com.university.election.service;

import com.university.election.exception.InvalidInputException;
import com.university.election.exception.ResourceNotFoundException;
import com.university.election.model.Student;
import com.university.election.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Student Service Implementation
 */
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;

    @Autowired
    public StudentServiceImpl(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Student createStudent(Student student) {
        validateStudent(student);
        student.setHasVoted(false); // New students haven't voted
        return repository.save(student);
    }

    @Override
    public Student getStudentById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    @Override
    public Student getStudentByStudentId(String studentId) {
        return repository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with student ID: " + studentId));
    }

    @Override
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    @Override
    public List<Student> getStudentsByVotingStatus(Boolean hasVoted) {
        return repository.findByVotingStatus(hasVoted);
    }

    @Override
    public Student updateStudent(Integer id, Student student) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        validateStudent(student);
        return repository.update(id, student);
    }

    @Override
    public Student markAsVoted(Integer id) {
        Student student = getStudentById(id);

        if (student.getHasVoted()) {
            throw new InvalidInputException("Student has already voted");
        }

        student.setHasVoted(true);
        return repository.update(id, student);
    }

    @Override
    public void deleteStudent(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public long countStudents() {
        return repository.count();
    }

    /**
     * Validate student data
     */
    private void validateStudent(Student student) {
        if (!student.validate()) {
            throw new InvalidInputException("Invalid student data: " + student.getValidationMessage());
        }

        if (!student.isEligible()) {
            throw new InvalidInputException("Student must be in year 1-4. Current year: " +
                    student.getYearOfStudy());
        }
    }
}