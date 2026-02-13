package com.university.election.controller;

import com.university.election.model.Candidate;
import com.university.election.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


 // Candidate REST Controller

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = "*")
public class CandidateController {

    private final CandidateService service;

    @Autowired
    public CandidateController(CandidateService service) {
        this.service = service;
    }

     // GET /api/candidates - Get all candidates
    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        List<Candidate> candidates = service.getAllCandidates();
        return ResponseEntity.ok(candidates);
    }

     // GET /api/candidates/{id} - Get candidate by ID
    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable Integer id) {
        Candidate candidate = service.getCandidateById(id);
        return ResponseEntity.ok(candidate);
    }

    // GET /api/candidates/election/{electionId} - Get candidates by election
    @GetMapping("/election/{electionId}")
    public ResponseEntity<List<Candidate>> getCandidatesByElection(@PathVariable Integer electionId) {
        List<Candidate> candidates = service.getCandidatesByElectionId(electionId);
        return ResponseEntity.ok(candidates);
    }

    // POST /api/candidates - Create new candidate
    @PostMapping
    public ResponseEntity<Candidate> createCandidate(@RequestBody Candidate candidate) {
        Candidate created = service.createCandidate(candidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/candidates/{id} - Update candidate
    @PutMapping("/{id}")
    public ResponseEntity<Candidate> updateCandidate(@PathVariable Integer id, @RequestBody Candidate candidate) {
        Candidate updated = service.updateCandidate(id, candidate);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/candidates/{id} - Delete candidate
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCandidate(@PathVariable Integer id) {
        service.deleteCandidate(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Candidate deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    // GET /api/candidates/count - Count candidates
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countCandidates() {
        long count = service.countCandidates();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}