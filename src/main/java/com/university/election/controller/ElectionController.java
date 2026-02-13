package com.university.election.controller;

import com.university.election.model.Election;
import com.university.election.service.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Election REST Controller
 * Handles HTTP requests for election management
 */
@RestController
@RequestMapping("/api/elections")
@CrossOrigin(origins = "*")
public class ElectionController {

    private final ElectionService service;

    @Autowired
    public ElectionController(ElectionService service) {
        this.service = service;
    }

    /**
     * GET /api/elections - Get all elections
     */
    @GetMapping
    public ResponseEntity<List<Election>> getAllElections() {
        List<Election> elections = service.getAllElections();
        return ResponseEntity.ok(elections);
    }

    /**
     * GET /api/elections/{id} - Get election by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Election> getElectionById(@PathVariable Integer id) {
        Election election = service.getElectionById(id);
        return ResponseEntity.ok(election);
    }

    /**
     * POST /api/elections - Create new election
     */
    @PostMapping
    public ResponseEntity<Election> createElection(@RequestBody Election election) {
        Election created = service.createElection(election);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/elections/{id} - Update election
     */
    @PutMapping("/{id}")
    public ResponseEntity<Election> updateElection(@PathVariable Integer id, @RequestBody Election election) {
        Election updated = service.updateElection(id, election);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/elections/{id} - Delete election
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteElection(@PathVariable Integer id) {
        service.deleteElection(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Election deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/elections/count - Count elections
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countElections() {
        long count = service.countElections();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}