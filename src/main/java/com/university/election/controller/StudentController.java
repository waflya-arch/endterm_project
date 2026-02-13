package com.university.election.controller;

import com.university.election.model.Student;
import com.university.election.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Student REST Controller
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService service;

    @Autowired
    public StudentController(StudentService service) {
        this.service = service;
    }

    /**
     * GET /api/students - Get all students
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = service.getAllStudents();
        return ResponseEntity.ok(students);
    }

    /**
     * GET /api/students/{id} - Get student by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer id) {
        Student student = service.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    /**
     * GET /api/students/studentId/{studentId} - Get student by student ID
     */
    @GetMapping("/studentId/{studentId}")
    public ResponseEntity<Student> getStudentByStudentId(@PathVariable String studentId) {
        Student student = service.getStudentByStudentId(studentId);
        return ResponseEntity.ok(student);
    }

    /**
     * GET /api/students/voted/{hasVoted} - Get students by voting status
     */
    @GetMapping("/voted/{hasVoted}")
    public ResponseEntity<List<Student>> getStudentsByVotingStatus(@PathVariable Boolean hasVoted) {
        List<Student> students = service.getStudentsByVotingStatus(hasVoted);
        return ResponseEntity.ok(students);
    }

    /**
     * POST /api/students - Create new student
     */
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student created = service.createStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/students/{id} - Update student
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Integer id, @RequestBody Student student) {
        Student updated = service.updateStudent(id, student);
        return ResponseEntity.ok(updated);
    }

    /**
     * POST /api/students/{id}/vote - Mark student as voted
     */
    @PostMapping("/{id}/vote")
    public ResponseEntity<Student> markAsVoted(@PathVariable Integer id) {
        Student updated = service.markAsVoted(id);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/students/{id} - Delete student
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable Integer id) {
        service.deleteStudent(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/students/count - Count students
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countStudents() {
        long count = service.countStudents();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}