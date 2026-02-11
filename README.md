# University President Election Management System - ENDTERM PROJECT
## RESTful API with Design Patterns, Component Principles & SOLID Architecture

**Building on Assignment 3 (JDBC) and Assignment 4 (SOLID + Advanced OOP)**

---

## Project Overview

Transforming existing **University President Election Management System** into a production-ready **Spring Boot RESTful API** while integrating:
- ✅ **Design Patterns** (Singleton, Factory, Builder)
- ✅ **Component Principles** (REP, CCP, CRP)
- ✅ **RESTful API Architecture**
- ✅ **SOLID Principles** (from Assignment 4)
- ✅ **Advanced OOP Features** (Generics, Lambdas, Reflection)
- ✅ **JDBC Database Integration** (from Assignment 3)

---

## Table of Contents

1. [Design Patterns Implementation](#design-patterns-implementation)
2. [Component Principles](#component-principles)
3. [REST API Documentation](#rest-api-documentation)
4. [System Architecture](#system-architecture)
5. [Database Schema](#database-schema)
6. [How to Run](#how-to-run)
7. [Integration with Your Existing Project](#integration-guide)
8. [SOLID & OOP Summary](#solid--oop-summary)
9. [Reflection](#reflection)

---

## Design Patterns Implementation

### 1. Singleton Pattern (3 Implementations)

#### Purpose
Ensure only one instance exists throughout the application for shared resources.

#### a) ElectionConfig - Application Configuration
**Location**: `patterns/singleton/ElectionConfig.java`

**Purpose**: Manages application-wide configuration for the election system

```java
// Thread-safe singleton using double-checked locking
ElectionConfig config = ElectionConfig.getInstance();

// Get election rules
int maxCandidates = config.getMaxCandidatesPerElection(); // Returns 10
int minYear = config.getMinYearForCandidate(); // Returns 2
boolean votingEnabled = config.isVotingEnabled(); // Returns true
```

**Configuration managed**:
- Maximum candidates per election
- Minimum/maximum year for candidates (2-4)
- Minimum/maximum year for voters (1-4)
- Voting status (enabled/disabled)

#### b) DatabaseConfig - Database Configuration Manager
**Location**: `patterns/singleton/DatabaseConfig.java`

**Purpose**: Centralized database connection management

```java
DatabaseConfig dbConfig = DatabaseConfig.getInstance();

// Set Spring DataSource (done automatically by Spring Boot)
dbConfig.setDataSource(dataSource);

// Get connection information
String info = dbConfig.getConnectionInfo();
// Returns: "Database: jdbc:postgresql://localhost:5432/university_election, User: postgres"

// Get database connection
Connection conn = dbConfig.getConnection();
```

**Benefits**:
- Single source of truth for database configuration
- Consistent connection handling across repositories
- Easy to update database settings

#### c) AuditLogger - Logging Service
**Location**: `patterns/singleton/AuditLogger.java`

**Purpose**: Centralized audit logging for election system

```java
AuditLogger logger = AuditLogger.getInstance();

// Log different levels
logger.info("Election created successfully");
logger.error("Database connection failed", exception);
logger.warn("Student has already voted");

// Log election-specific actions
logger.logVote(studentId, candidateId, electionId);
logger.logCandidateRegistration("John Doe", electionId);
```

**Logged actions**:
- Election creation/updates
- Vote casting
- Candidate registration
- System errors
- All CRUD operations

**Why Singleton for These?**
- **ElectionConfig**: System-wide rules that shouldn't change
- **DatabaseConfig**: One connection pool for entire application
- **AuditLogger**: Single log file, consistent formatting

---

### 2. Factory Pattern

#### Purpose
Create different types of BaseEntity objects (Candidate, Student) while maintaining a common interface.

#### Implementation
**Location**: `patterns/factory/EntityFactory.java`

**Class Hierarchy**:
```
BaseEntity (abstract)
    ├── Candidate (Year 2-4, has platform, votes)
    └── Student (Year 1-4, can vote, has studentId)
```

#### Usage Examples:

**Creating entities**:
```java
// Simple creation
BaseEntity candidate = EntityFactory.createEntity(
    EntityType.CANDIDATE, 
    "Ainaz Sanatbayeva"
);

// Detailed candidate creation
BaseEntity candidate = EntityFactory.createCandidate(
    "Ainaz Sanatbayeva",  // name
    3,                     // year
    "Improve campus wifi and add more study spaces"  // platform
);

// Candidate with election association
BaseEntity candidate = EntityFactory.createCandidate(
    "Ainaz Sanatbayeva",
    3,
    "Campus improvement platform",
    electionId  // Associated with specific election
);

// Student creation
BaseEntity student = EntityFactory.createStudent(
    "Dias Nurtay",
    "ST001",   // studentId
    2,         // year
    "CS"       // major
);
```

**Validated creation** (with business rule checks):
```java
try {
    // Will throw exception if year < 2 or year > 4
    BaseEntity candidate = EntityFactory.createValidatedCandidate(
        "Invalid Candidate",
        1,  // Year 1 not allowed for candidates!
        "Some platform"
    );
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
    // Output: "Candidates must be in years 2-4"
}
```

#### Polymorphism in Action:
```java
// Both Candidate and Student can be treated as BaseEntity
List<BaseEntity> entities = new ArrayList<>();
entities.add(EntityFactory.createCandidate("Alice", 3, "Platform A"));
entities.add(EntityFactory.createStudent("Bob", "ST002", 2, "EE"));

// Polymorphic behavior
for (BaseEntity entity : entities) {
    entity.displayInfo();       // Calls appropriate implementation
    boolean eligible = entity.isEligible();  // Candidate: year 2-4, Student: year 1-4
    String type = entity.getEntityType();     // "Candidate" or "Student"
}
```

#### Benefits:
- ✅ **Centralized creation logic**: All entity creation goes through factory
- ✅ **Easy to extend**: Add new entity types without modifying existing code
- ✅ **Validation**: Factory can validate before creating objects
- ✅ **Logging**: Automatic logging of all entity creation
- ✅ **Polymorphism**: Returns BaseEntity, supporting flexible code

---

### 3. Builder Pattern

#### Purpose
Construct complex Election objects with many optional parameters using fluent method calls.

#### Implementation
**Location**: `model/Election.java` (nested ElectionBuilder class)

#### Problem It Solves:
Elections have many fields (11+ fields). Without builder, we'd need:
- Multiple constructors (constructor explosion)
- Many parameters in one constructor (error-prone)
- Setters everywhere (mutable, error-prone)

#### Usage Examples:

**Basic election**:
```java
Election election = new Election.ElectionBuilder(
    "Spring 2024 Presidential Election",  // name (required)
    LocalDate.of(2024, 3, 1),            // start date (required)
    LocalDate.of(2024, 3, 15)            // end date (required)
).build();
```

**Detailed election with optional parameters**:
```java
Election election = new Election.ElectionBuilder(
        "Fall 2024 Presidential Election",
        LocalDate.of(2024, 9, 1),
        LocalDate.of(2024, 9, 30)
    )
    .description("Annual presidential election for student body")
    .location("Main Campus - Student Center")
    .electionType("PRESIDENT")
    .status("SCHEDULED")
    .votingOpen(false)
    .totalVotes(0)
    .totalCandidates(0)
    .build();  // Validates and creates Election
```

**Fluent API - add parameters as needed**:
```java
Election.ElectionBuilder builder = new Election.ElectionBuilder(
    "Election Name",
    startDate,
    endDate
);

// Add optional parameters one by one
if (needsDescription) {
    builder.description("Some description");
}

if (customLocation) {
    builder.location("Custom Location");
}

// Build when ready
Election election = builder.build();
```

**Validation built-in**:
```java
try {
    Election invalid = new Election.ElectionBuilder(
        "Invalid Election",
        LocalDate.of(2024, 3, 15),  // End date
        LocalDate.of(2024, 3, 1)    // Start date - BEFORE end date!
    ).build();
} catch (IllegalStateException e) {
    System.out.println("Error: " + e.getMessage());
    // Output: "End date cannot be before start date"
}
```

#### Benefits:
-  **Readable**: Clear what each parameter represents
- **Flexible**: Only set parameters you need
- **Immutable**: Election object can't be modified after creation
-  **Validation**: Checks business rules before creating object
-  **No constructor explosion**: One builder handles all combinations

---

## Component Principles

### REP - Reuse/Release Equivalence Principle
**"The granule of reuse is the granule of release"**

**Implementation in our project**:

```
patterns/              # Reusable design pattern implementations
├── singleton/        # Can be reused in any Spring Boot project
│   ├── ElectionConfig
│   ├── DatabaseConfig
│   └── AuditLogger
├── factory/          # Can be released as a separate module
│   └── EntityFactory
└── builder/          # (In Election.java, can be extracted)

repository/           # Reusable data access layer
├── interfaces/       # Generic CRUD operations
└── impl/             # JDBC implementations

service/              # Reusable business logic layer
utils/                # Utility classes (ReflectionUtils, SortingUtils)
```

**Example**: The `patterns` package could be published as a Maven dependency and reused in other projects.

### CCP - Common Closure Principle
**"Classes that change together are packaged together"**

**Implementation**:

```
exception/                      # Changes to exception handling affect all these
├── GlobalExceptionHandler     # If we add new exception type, we update here
├── ResourceNotFoundException  
├── BusinessException
└── ValidationException

dto/                           # Changes to API contract affect all DTOs
├── ElectionDTO
├── CandidateDTO
├── StudentDTO
└── VoteDTO

model/                         # Changes to domain model affect all entities
├── BaseEntity
├── Election
├── Candidate
└── Student
```

**Why this matters**:
- If we change how exceptions work, all exception-related code is in one place
- If API contract changes, all DTOs are together
- Reduces scattered changes across the codebase

### CRP - Common Reuse Principle
**"Don't force users of a component to depend on things they don't need"**

**Implementation**:

**CandidateRepository doesn't depend on StudentRepository**:
```java
// CandidateRepository interface - focused, no unnecessary dependencies
public interface CandidateRepository {
    Candidate save(Candidate candidate);
    List<Candidate> findByElectionId(Integer electionId);
    // NO student-related methods here
}

// StudentRepository interface - separate concerns
public interface StudentRepository {
    Student save(Student student);
    List<Student> findEligibleVoters();
    // NO candidate-related methods here
}
```

**Service layer follows same principle**:
```java
// ElectionService only depends on what it needs
public class ElectionServiceImpl {
    private final ElectionRepository electionRepository;  // ONLY election repo
    // Does NOT depend on CandidateService, StudentService, etc.
}
```

**Why this matters**:
- Changes to Student code don't affect Candidate code
- Can test Candidate functionality without Student dependencies
- Cleaner, more maintainable code

---

## REST API Documentation

### Base URL
```
http://localhost:8080/api
```

### Elections API

#### 1. Get All Elections
```http
GET /api/elections
```

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "name": "Spring 2024 Presidential Election",
    "startDate": "2024-03-01",
    "endDate": "2024-03-15",
    "description": "Annual presidential election",
    "status": "ACTIVE",
    "totalVotes": 150,
    "totalCandidates": 4,
    "location": "Main Campus",
    "electionType": "PRESIDENT",
    "votingOpen": true,
    "isActive": true
  }
]
```

#### 2. Get Election by ID
```http
GET /api/elections/{id}
```

**Example**: `GET /api/elections/1`

**Response**: `200 OK`
```json
{
  "id": 1,
  "name": "Spring 2024 Presidential Election",
  "startDate": "2024-03-01",
  "endDate": "2024-03-15",
  "status": "ACTIVE",
  "totalVotes": 150,
  "totalCandidates": 4,
  "votingOpen": true
}
```

**Error Response**: `404 Not Found`
```json
{
  "timestamp": "2024-02-11T10:30:00",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Election not found with id : '999'",
  "path": "/api/elections/999"
}
```

#### 3. Create New Election (using Builder Pattern)
```http
POST /api/elections
Content-Type: application/json
```

**Request Body**:
```json
{
  "name": "Fall 2024 Presidential Election",
  "startDate": "2024-09-01",
  "endDate": "2024-09-30",
  "description": "Annual fall election",
  "location": "Student Center",
  "electionType": "PRESIDENT"
}
```

**Response**: `201 Created`
```json
{
  "id": 2,
  "name": "Fall 2024 Presidential Election",
  "startDate": "2024-09-01",
  "endDate": "2024-09-30",
  "description": "Annual fall election",
  "status": "SCHEDULED",
  "totalVotes": 0,
  "totalCandidates": 0,
  "location": "Student Center",
  "electionType": "PRESIDENT",
  "votingOpen": false,
  "isActive": false
}
```

#### 4. Update Election
```http
PUT /api/elections/{id}
Content-Type: application/json
```

**Request Body**:
```json
{
  "name": "Updated Election Name",
  "description": "Updated description",
  "status": "ACTIVE",
  "votingOpen": true
}
```

**Response**: `200 OK` (returns updated election)

#### 5. Delete Election
```http
DELETE /api/elections/{id}
```

**Response**: `200 OK`
```json
{
  "message": "Election deleted successfully",
  "electionId": "1"
}
```

#### 6. Open Voting
```http
PATCH /api/elections/{id}/open
```

**Response**: `200 OK`
```json
{
  "message": "Voting opened for election",
  "electionId": "1",
  "votingOpen": true
}
```

#### 7. Close Voting
```http
PATCH /api/elections/{id}/close
```

**Response**: `200 OK`
```json
{
  "message": "Voting closed for election",
  "electionId": "1",
  "votingOpen": false
}
```

### Candidates API

#### 1. Get All Candidates
```http
GET /api/candidates
```

#### 2. Get Candidates by Election
```http
GET /api/candidates/election/{electionId}
```

**Response**:
```json
[
  {
    "id": 1,
    "name": "Ainaz Sanatbayeva",
    "year": 3,
    "platform": "Improve campus facilities",
    "electionId": 1,
    "voteCount": 45,
    "eligible": true
  },
  {
    "id": 2,
    "name": "Dias Nurtay",
    "year": 4,
    "platform": "Better student services",
    "electionId": 1,
    "voteCount": 38,
    "eligible": true
  }
]
```

#### 3. Create Candidate (using Factory Pattern)
```http
POST /api/candidates
Content-Type: application/json
```

**Request Body**:
```json
{
  "name": "New Candidate",
  "year": 3,
  "platform": "Campaign platform description",
  "electionId": 1
}
```

**Response**: `201 Created`

**Validation Error** (Year 1 candidate):
```json
{
  "timestamp": "2024-02-11T10:30:00",
  "status": 400,
  "error": "Business Rule Violation",
  "message": "Candidates must be in years 2-4",
  "path": "/api/candidates"
}
```

#### 4. Get Candidate Results
```http
GET /api/candidates/{id}/results
```

**Response**:
```json
{
  "candidateId": 1,
  "name": "Ainaz Sanatbayeva",
  "voteCount": 45,
  "percentage": 30.0,
  "rank": 1
}
```

### Students API

#### 1. Get All Students
```http
GET /api/students
```

#### 2. Get Eligible Voters
```http
GET /api/students/eligible
```

**Response**:
```json
[
  {
    "id": 1,
    "name": "Student Name",
    "studentId": "ST001",
    "year": 2,
    "major": "CS",
    "hasVoted": false,
    "canVote": true,
    "eligible": true
  }
]
```

#### 3. Cast Vote
```http
POST /api/votes
Content-Type: application/json
```

**Request Body**:
```json
{
  "studentId": 1,
  "candidateId": 2,
  "electionId": 1
}
```

**Response**: `201 Created`
```json
{
  "message": "Vote cast successfully",
  "studentId": 1,
  "candidateId": 2,
  "electionId": 1,
  "timestamp": "2024-02-11T10:30:00"
}
```

**Error - Already Voted**:
```json
{
  "timestamp": "2024-02-11T10:30:00",
  "status": 400,
  "error": "Business Rule Violation",
  "message": "Student has already voted in this election",
  "path": "/api/votes"
}
```

---

## 📊 System Architecture

### Layered Architecture

```
┌──────────────────────────────────────────────────────────┐
│                  REST API Layer                          │
│  ┌────────────────────────────────────────────────────┐  │
│  │  ElectionController, CandidateController           │  │
│  │  StudentController, VoteController                 │  │
│  │  - Handle HTTP requests                            │  │
│  │  - Return JSON responses                           │  │
│  │  - NO business logic                               │  │
│  └────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────┘
                         ↓ ↑ (DTOs)
┌──────────────────────────────────────────────────────────┐
│                   Service Layer                          │
│  ┌────────────────────────────────────────────────────┐  │
│  │  ElectionService, CandidateService                 │  │
│  │  - Business logic and validation                   │  │
│  │  - Uses Factory Pattern for entity creation       │  │
│  │  - Uses Builder Pattern for complex objects       │  │
│  │  - Implements SOLID principles                    │  │
│  └────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────┘
                         ↓ ↑ (Entities)
┌──────────────────────────────────────────────────────────┐
│                 Repository Layer                         │
│  ┌────────────────────────────────────────────────────┐  │
│  │  ElectionRepository, CandidateRepository (JDBC)   │  │
│  │  - Database operations only                        │  │
│  │  - CRUD operations                                 │  │
│  │  - Uses DatabaseConfig Singleton                  │  │
│  └────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────┘
                         ↓ ↑ (SQL/JDBC)
┌──────────────────────────────────────────────────────────┐
│                   Database Layer                         │
│               PostgreSQL Database                        │
│     elections | candidates | students | votes            │
└──────────────────────────────────────────────────────────┘

        ┌────────────────────────────────────┐
        │   Singleton Services (Shared)      │
        │  - ElectionConfig                  │
        │  - DatabaseConfig                  │
        │  - AuditLogger                     │
        └────────────────────────────────────┘
```

### Request Flow Example: Creating a Candidate

```
1. Client → POST /api/candidates
   ↓
2. CandidateController.createCandidate(candidateDTO)
   ↓
3. CandidateService.createCandidate(candidateDTO)
   - Validates data
   - Uses EntityFactory.createValidatedCandidate() [Factory Pattern]
   - AuditLogger logs candidate creation [Singleton]
   ↓
4. CandidateRepository.save(candidate) [JDBC]
   - Uses DatabaseConfig.getConnection() [Singleton]
   - Executes SQL INSERT
   ↓
5. Response ← 201 Created + CandidateDTO
```

---

## 💾 Database Schema

Your existing schema is perfect! Just ensure these tables exist:

```sql
-- elections table
CREATE TABLE elections (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    description TEXT,
    status VARCHAR(50) DEFAULT 'SCHEDULED',
    total_votes INTEGER DEFAULT 0,
    total_candidates INTEGER DEFAULT 0,
    location VARCHAR(255),
    election_type VARCHAR(50),
    voting_open BOOLEAN DEFAULT FALSE
);

-- candidates table
CREATE TABLE candidates (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    year INTEGER NOT NULL CHECK (year >= 2 AND year <= 4),
    platform TEXT,
    election_id INTEGER REFERENCES elections(id) ON DELETE CASCADE,
    vote_count INTEGER DEFAULT 0
);

-- students table
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    student_id VARCHAR(50) UNIQUE NOT NULL,
    year INTEGER NOT NULL CHECK (year >= 1 AND year <= 4),
    major VARCHAR(100),
    has_voted BOOLEAN DEFAULT FALSE
);

-- votes table (for tracking who voted)
CREATE TABLE votes (
    id SERIAL PRIMARY KEY,
    student_id INTEGER REFERENCES students(id),
    candidate_id INTEGER REFERENCES candidates(id),
    election_id INTEGER REFERENCES elections(id),
    vote_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(student_id, election_id)
);
```

---

## 🚀 How to Run

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL
- Your existing database: `university_election`

### Steps

1. **Update application.properties**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/university_election
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn spring-boot:run
```

4. **Access the API**
```
http://localhost:8080/api/elections
```

### Testing with curl

```bash
# Get all elections
curl http://localhost:8080/api/elections

# Create election using Builder Pattern
curl -X POST http://localhost:8080/api/elections \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Election",
    "startDate": "2024-03-01",
    "endDate": "2024-03-15"
  }'

# Create candidate using Factory Pattern
curl -X POST http://localhost:8080/api/candidates \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Candidate",
    "year": 3,
    "platform": "Test Platform",
    "electionId": 1
  }'
```

---



## SOLID & OOP Summary

### SOLID Principles (Already in Your Code!)

- **SRP**: Each class has one responsibility
- **OCP**: BaseEntity is open for extension (Candidate, Student)
- **LSP**: Candidate and Student can substitute BaseEntity
- **ISP**: Focused interfaces (Validatable, Votable)
- **DIP**: Services depend on repository interfaces

### Advanced OOP Features (Already in Your Code!)

- **Generics**: `CrudRepository<T, ID>`
- **Lambdas**: `SortingUtils` with lambda expressions
- **Reflection**: `ReflectionUtils` for runtime inspection
- **Default Methods**: In your Validatable interface

### New Additions for Endterm

- **Factory Pattern**: EntityFactory for creating Candidate/Student
- **Builder Pattern**: Election.ElectionBuilder for complex objects
- **Singleton Pattern**: ElectionConfig, DatabaseConfig, AuditLogger
- **REST Architecture**: Controllers, DTOs, JSON responses

---

## 🎓 Reflection

### What I Learned

#### 1. Design Patterns in Real Systems
- **Singleton** is perfect for shared configuration and logging
- **Factory** makes entity creation clean and testable
- **Builder** solves the problem of many constructor parameters

#### 2. Integration of Multiple Concepts
- SOLID principles work hand-in-hand with design patterns
- REST API structure naturally follows layered architecture
- Component principles help organize a growing codebase

#### 3. Practical Spring Boot
- Spring Boot simplifies configuration dramatically
- Dependency injection makes code more testable
- REST controllers separate HTTP concerns from business logic

### Challenges Faced

1. **Adapting Existing Code**: Transforming console app to REST API required rethinking
2. **Design Pattern Integration**: Finding the right places to use each pattern
3. **Maintaining SOLID**: Keeping principles while adding new features

### Value Added

This endterm project adds:
- **Professional API**: Can be consumed by any frontend
- **Better Architecture**: Design patterns improve maintainability
- **Production Ready**: Proper logging, exception handling
- **Testable**: Clean separation makes testing easier

---

## Project Structure

```
election-api-endterm/
├── src/main/java/com/university/election/
│   ├── controller/              # REST Controllers
│   ├── service/                 # Business Logic
│   ├── repository/              # Data Access (JDBC)
│   ├── model/                   # Domain Models
│   │   ├── BaseEntity.java      # Abstract base
│   │   ├── Candidate.java
│   │   ├── Student.java
│   │   └── Election.java        # With Builder
│   ├── dto/                     # API DTOs
│   ├── exception/               # Exception Handling
│   ├── patterns/                # Design Patterns
│   │   ├── singleton/          # 3 Singletons
│   │   │   ├── ElectionConfig
│   │   │   ├── DatabaseConfig
│   │   │   └── AuditLogger
│   │   └── factory/            # Factory
│   │       └── EntityFactory
│   └── Application.java         # Spring Boot Main
├── src/main/resources/
│   ├── application.properties
│   └── schema.sql
├── pom.xml
└── README.md
```

---
