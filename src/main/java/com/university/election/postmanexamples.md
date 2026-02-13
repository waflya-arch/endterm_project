# Postman Collection - University Election API

## Quick Test Requests

### 1. Create Election
```
POST http://localhost:8080/api/elections
Content-Type: application/json

{
  "name": "University President Election 2026",
  "startDate": "2026-01-10",
  "endDate": "2026-01-19",
  "academicYear": "2025-2026"
}
```

### 2. Get All Elections
```
GET http://localhost:8080/api/elections
```

### 3. Create Candidate (Valid - Year 2)
```
POST http://localhost:8080/api/candidates
Content-Type: application/json

{
  "name": "Bekbolat Aruzhan",
  "faculty": "Software Engineering",
  "yearOfStudy": 2,
  "campaign": "New learning platforms for better education",
  "election": {
    "id": 1
  }
}
```

### 4. Create Candidate (Invalid - Year 1) - Should Fail
```
POST http://localhost:8080/api/candidates
Content-Type: application/json

{
  "name": "Invalid Candidate",
  "faculty": "Computer Science",
  "yearOfStudy": 1,
  "campaign": "This should fail",
  "election": {
    "id": 1
  }
}
```
Expected: 400 Bad Request with error message

### 5. Get Candidates by Election
```
GET http://localhost:8080/api/candidates/election/1
```

### 6. Create Student
```
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "name": "Arguan Bakikair",
  "studentId": "S001",
  "faculty": "Software Engineering",
  "yearOfStudy": 1
}
```

### 7. Mark Student as Voted
```
POST http://localhost:8080/api/students/1/vote
```

### 8. Try to Vote Again (Should Fail)
```
POST http://localhost:8080/api/students/1/vote
```
Expected: 400 Bad Request - "Student has already voted"

### 9. Get Students Who Haven't Voted
```
GET http://localhost:8080/api/students/voted/false
```

### 10. Get Students Who Have Voted
```
GET http://localhost:8080/api/students/voted/true
```

### 11. Create Duplicate Student ID (Should Fail)
```
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "name": "Another Student",
  "studentId": "S001",
  "faculty": "Computer Science",
  "yearOfStudy": 2
}
```
Expected: 409 Conflict - Duplicate student ID

### 12. Update Election
```
PUT http://localhost:8080/api/elections/1
Content-Type: application/json

{
  "name": "University President Election 2026 - Updated",
  "startDate": "2026-01-12",
  "endDate": "2026-01-20",
  "academicYear": "2025-2026"
}
```

### 13. Delete Election (with cascade)
```
DELETE http://localhost:8080/api/elections/2
```
Note: This will also delete all candidates for this election (CASCADE)

### 14. Get Non-Existent Resource (Should Fail)
```
GET http://localhost:8080/api/elections/999
```
Expected: 404 Not Found

### 15. Count Endpoints
```
GET http://localhost:8080/api/elections/count
GET http://localhost:8080/api/candidates/count
GET http://localhost:8080/api/students/count
```

## Testing Validation

### Invalid Date Range
```
POST http://localhost:8080/api/elections
Content-Type: application/json

{
  "name": "Invalid Election",
  "startDate": "2026-01-20",
  "endDate": "2026-01-10",
  "academicYear": "2025-2026"
}
```
Expected: 400 Bad Request - "Start date must be before end date"

### Empty Name
```
POST http://localhost:8080/api/elections
Content-Type: application/json

{
  "name": "",
  "startDate": "2026-01-10",
  "endDate": "2026-01-20",
  "academicYear": "2025-2026"
}
```
Expected: 400 Bad Request - "Election name cannot be empty"

### Candidate with Non-Existent Election
```
POST http://localhost:8080/api/candidates
Content-Type: application/json

{
  "name": "Test Candidate",
  "faculty": "CS",
  "yearOfStudy": 3,
  "campaign": "Test",
  "election": {
    "id": 999
  }
}
```
Expected: 404 Not Found - "Election not found with id: 999"