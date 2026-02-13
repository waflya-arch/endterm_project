package com.university.election.service;

import com.university.election.model.Student;

import java.util.List;

/**
 * Student Service Interface
 */
public interface StudentService {
    Student createStudent(Student student);
    Student getStudentById(Integer id);
    Student getStudentByStudentId(String studentId);
    List<Student> getAllStudents();
    List<Student> getStudentsByVotingStatus(Boolean hasVoted);
    Student updateStudent(Integer id, Student student);
    Student markAsVoted(Integer id);
    void deleteStudent(Integer id);
    long countStudents();
}