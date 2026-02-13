# University President Election Management System - REST API
## Endterm Project: Design Patterns, Component Principles & RESTful API

---

## A. Project Overview

### Purpose
A complete Spring Boot REST API for managing university president elections. The system handles elections, candidates running for president, and student voters with full CRUD operations, design patterns, and SOLID principles.

### Key Features
- **RESTful API** with JSON request/response
- **Design Patterns**: Singleton, Factory, Builder
- **SOLID Principles** throughout the architecture
- **Advanced OOP**: Inheritance, Polymorphism, Interfaces, Composition
- **Generics** in repository layer
- **Lambda Expressions** for functional programming
- **Reflection/RTTI** for runtime inspection
- **Exception Handling** with global error responses

### Entities and Relationships
- **Election**: University president election with dates and academic year
- **Candidate**: Students running for president (year 2-4 only) - contains Election
- **Student**: All students who can vote (year 1-4)

---

## B. REST API Documentation

### Base URL
```
http://localhost:8080/api
```

### Election Endpoints

#### 1. Get All Elections
```http
GET /api/elections
```
**Response:**
```json
[
  {
    "id": 1,
    "name": "University President Election 2026",
    "startDate": "2026-01-10",
    "endDate": "2026-01-19",
    "academicYear": "2025-2026"
  }
]
```

#### 2. Get Election by ID
```http
GET /api/elections/{id}
```

#### 3. Create Election
```http
POST /api/elections
Content-Type: application/json

{
  "name": "University President Election 2027",
  "startDate": "2027-01-10",
  "endDate": "2027-01-19",
  "academicYear": "2026-2027"
}
```

#### 4. Update Election
```http
PUT /api/elections/{id}
Content-Type: application/json

{
  "name": "Updated Election Name",
  "startDate": "2027-01-15",
  "endDate": "2027-01-25",
  "academicYear": "2026-2027"
}
```

#### 5. Delete Election
```http
DELETE /api/elections/{id}
```

### Candidate Endpoints

#### 1. Get All Candidates
```http
GET /api/candidates
```

#### 2. Get Candidates by Election
```http
GET /api/candidates/election/{electionId}
```

#### 3. Create Candidate
```http
POST /api/candidates
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

**Note:** Candidates must be in year 2, 3, or 4. Year 1 students will trigger validation error.

#### 4. Update Candidate
```http
PUT /api/candidates/{id}
```

#### 5. Delete Candidate
```http
DELETE /api/candidates/{id}
```

### Student Endpoints

#### 1. Get All Students
```http
GET /api/students
```

#### 2. Get Student by ID
```http
GET /api/students/{id}
```

#### 3. Get Student by Student ID
```http
GET /api/students/studentId/{studentId}
```

#### 4. Get Students by Voting Status
```http
GET /api/students/voted/false  # Get students who haven't voted
GET /api/students/voted/true   # Get students who have voted
```

#### 5. Create Student
```http
POST /api/students
Content-Type: application/json

{
  "name": "Arguan Bakikair",
  "studentId": "S001",
  "faculty": "Software Engineering",
  "yearOfStudy": 1,
  "hasVoted": false
}
```

#### 6. Mark Student as Voted
```http
POST /api/students/{id}/vote
```

#### 7. Update Student
```http
PUT /api/students/{id}
```

#### 8. Delete Student
```http
DELETE /api/students/{id}
```

### Error Responses

All errors return JSON with standard format:

```json
{
  "timestamp": "2026-02-12T10:30:45",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Election not found with id: 999",
  "path": "/api/elections/999"
}
```

**Status Codes:**
- `200 OK` - Success
- `201 Created` - Resource created
- `400 Bad Request` - Invalid input
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate resource
- `500 Internal Server Error` - Database error

---

## C. Design Patterns

### 1. Singleton Pattern
**Purpose:** Ensure single instance of configuration and logging services

**Implementation:**
- `DatabaseConfig` - Database configuration manager
- `AppLogger` - Application-wide logging service

**Location:** `src/main/java/com/university/election/patterns/`

**Usage Example:**
```java
DatabaseConfig config = DatabaseConfig.getInstance();
config.displayConfiguration();

AppLogger logger = AppLogger.getInstance();
logger.info("Application started");
```

**Why Singleton?**
- Configuration should be consistent across the application
- Logger should maintain single state and output stream
- Prevents multiple instances consuming unnecessary resources

### 2. Factory Pattern
**Purpose:** Create different types of BaseEntity subclasses polymorphically

**Implementation:** `EntityFactory`

**Location:** `src/main/java/com/university/election/patterns/EntityFactory.java`

**Usage Example:**
```java
// Create candidate
BaseEntity candidate = factory.createEntity(
    EntityType.CANDIDATE,
    1, "Kairat", "CS", 3, "Better campus!", election
);

// Create student
BaseEntity student = factory.createEntity(
    EntityType.STUDENT,
    1, "Vladimir", "S001", "SE", 2, false
);

// Polymorphic behavior
candidate.displayInfo();
student.displayInfo();
```

**Benefits:**
- Encapsulates object creation logic
- Easy to extend with new entity types (Open-Closed Principle)
- Returns base type for polymorphic usage

### 3. Builder Pattern
**Purpose:** Construct complex Election objects with fluent API

**Implementation:** `ElectionBuilder`

**Location:** `src/main/java/com/university/election/patterns/ElectionBuilder.java`

**Usage Example:**
```java
Election election = ElectionBuilder.builder()
    .withName("University President Election 2026")
    .withDateRange(LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 19))
    .withAcademicYear("2025-2026")
    .build();
```

**Benefits:**
- Fluent, readable API for object construction
- Handles validation during build
- Makes complex object creation simple
- Optional parameters support

---

## D. Component Principles

### REP (Reuse/Release Equivalence Principle)
The system is organized into reusable, cohesive modules:

**Reusable Components:**
- `patterns/` - Design pattern implementations (Singleton, Factory, Builder)
- `repository/` - Generic CRUD repository with JDBC
- `service/` - Business logic layer with interfaces
- `utils/` - Reflection and sorting utilities

These components can be:
- Released independently
- Reused in other projects
- Versioned separately

### CCP (Common Closure Principle)
Classes that change together are grouped together:

**Package Organization:**
- `model/` - All entity classes (Candidate, Student, Election)
- `repository/` - All data access classes
- `service/` - All business logic classes
- `controller/` - All REST endpoint handlers
- `exception/` - All custom exceptions

**Example:** If election business rules change, only `service/ElectionServiceImpl` needs modification, not scattered across packages.

### CRP (Common Reuse Principle)
No dependency on unused classes:

**Clean Dependencies:**
- Controllers depend ONLY on service interfaces
- Services depend ONLY on repository interfaces
- Repositories depend ONLY on Spring JDBC

**No Forced Dependencies:**
- Election service doesn't depend on Student classes
- Utilities are standalone with no cross-dependencies
- Pattern classes are independent modules

---

## E. SOLID Principles

### 1. Single Responsibility Principle (SRP)
Each class has one reason to change:

- `ElectionController` - Handle HTTP requests only
- `ElectionService` - Business logic and validation
- `ElectionRepository` - Database operations only
- `GlobalExceptionHandler` - Error response formatting

### 2. Open-Closed Principle (OCP)
Open for extension, closed for modification:

- `BaseEntity` abstract class - New subclasses can be added without changing base
- `CrudRepository<T, ID>` interface - New repository types implement interface
- `EntityFactory` - New entity types added via enum extension

### 3. Liskov Substitution Principle (LSP)
Subclasses can replace their base classes:

```java
BaseEntity entity = new Candidate(...);  // Works perfectly
entity.displayInfo();                    // Calls Candidate's implementation
entity.isEligible();                     // Calls Candidate's eligibility logic
```

Both `Candidate` and `Student` can be used wherever `BaseEntity` is expected.

### 4. Interface Segregation Principle (ISP)
Small, focused interfaces:

- `Validatable` - Only validation methods
- `Votable` - Only voting methods
- `CrudRepository<T, ID>` - Only CRUD operations

No class is forced to implement methods it doesn't use.

### 5. Dependency Inversion Principle (DIP)
Depend on abstractions, not concretions:

```java
// Controller depends on interface, not implementation
public class ElectionController {
    private final ElectionService service;  // Interface
}

// Service depends on interface, not implementation
public class ElectionServiceImpl implements ElectionService {
    private final ElectionRepository repository;  // Interface
}
```

---

## F. Advanced OOP Features

### 1. Generics
**Location:** `repository/CrudRepository.java`

```java
public interface CrudRepository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    T update(ID id, T entity);
    void deleteById(ID id);
}
```

**Usage:**
- Type-safe repository operations
- Reusable across all entity types
- Compile-time type checking

### 2. Lambda Expressions
**Location:** `utils/SortingUtils.java`

**Examples:**
```java
// Sorting with lambda
students.stream()
    .sorted((s1, s2) -> s1.getName().compareTo(s2.getName()))
    .collect(Collectors.toList());

// Filtering with lambda
students.stream()
    .filter(s -> !s.getHasVoted())
    .collect(Collectors.toList());

// Method reference
students.stream()
    .sorted(Comparator.comparing(Student::getYearOfStudy))
    .forEach(s -> System.out.println(s.getName()));
```

**Benefits:**
- Concise, functional code
- Stream processing
- Improved readability

### 3. Reflection / RTTI
**Location:** `utils/ReflectionUtils.java`

**Capabilities:**
```java
// Get class information
String className = ReflectionUtils.getClassName(candidate);
List<String> fields = ReflectionUtils.getFields(candidate);
List<String> methods = ReflectionUtils.getMethods(candidate);

// Print complete analysis
ReflectionUtils.printClassInfo(candidate);
```

**Output Example:**
```
=== Reflection Analysis ===
Class: Candidate
Package: com.university.election.model
Superclass: BaseEntity
Interfaces:
  - Validatable
Fields:
  - faculty
  - yearOfStudy
  - campaign
  - election
Methods:
  - getDescription
  - isEligible
  - validate
```

### 4. Interface Default and Static Methods
**Location:** `model/Validatable.java`, `model/Votable.java`

**Default Methods:**
```java
default String getValidationMessage() {
    return validate() ? "Valid" : "Invalid entity data";
}
```

**Static Methods:**
```java
static boolean isNotEmpty(String value) {
    return value != null && !value.trim().isEmpty();
}
```

---

## G. OOP Design Documentation

### Abstract Class: BaseEntity
```
BaseEntity (Abstract)
├── Fields:
│   ├── id: Integer
│   └── name: String
├── Abstract Methods:
│   ├── getDescription(): String
│   └── isEligible(): boolean
└── Concrete Method:
    └── displayInfo(): void
```

### Inheritance Hierarchy
```
        BaseEntity (Abstract)
              │
      ┌───────┴────────┐
      │                │
  Candidate        Student
      │                │
Implements:      Implements:
Validatable     Validatable, Votable
```

### Composition Relationship
```
Candidate ◆─── Election
```
A Candidate **contains** an Election object. Cannot create a candidate without an election.

### Interfaces
1. **Validatable**
    - `boolean validate()`
    - `default String getValidationMessage()`
    - `static boolean isNotEmpty(String)`

2. **Votable**
    - `void vote()`
    - `boolean canVote()`
    - `default String getVotingStatus()`

### Polymorphism Example
```java
// Polymorphic collection
List<BaseEntity> entities = new ArrayList<>();
entities.add(new Candidate(...));
entities.add(new Student(...));

// Polymorphic behavior
for (BaseEntity entity : entities) {
    entity.displayInfo();        // Calls overridden method
    System.out.println(entity.getDescription());  // Different implementations
}
```

---

## H. Database Schema

### Tables

**elections**
```sql
CREATE TABLE elections (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    academic_year VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**candidates**
```sql
CREATE TABLE candidates (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    faculty VARCHAR(255) NOT NULL,
    year_of_study INTEGER NOT NULL CHECK (year_of_study >= 2 AND year_of_study <= 4),
    campaign TEXT,
    election_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (election_id) REFERENCES elections(id) ON DELETE CASCADE
);
```

**students**
```sql
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    student_id VARCHAR(50) UNIQUE NOT NULL,
    faculty VARCHAR(255) NOT NULL,
    year_of_study INTEGER NOT NULL CHECK (year_of_study >= 1 AND year_of_study <= 4),
    has_voted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Constraints
- **Primary Keys**: All tables use SERIAL
- **Foreign Key**: `candidates.election_id` → `elections.id` (CASCADE)
- **Unique**: `students.student_id`
- **Check**: Candidates year 2-4, Students year 1-4

---

## I. System Architecture

### Layered Architecture
```
┌──────────────────────────────────────────┐
│         REST Controllers                 │
│   (ElectionController, etc.)            │
│   - Handle HTTP requests                │
│   - No business logic                   │
└────────────┬────────────────────────────┘
             │ depends on
             ▼
┌──────────────────────────────────────────┐
│         Service Layer                    │
│   (ElectionService, etc.)               │
│   - Business logic                      │
│   - Validation rules                    │
│   - Transaction management              │
└────────────┬────────────────────────────┘
             │ depends on
             ▼
┌──────────────────────────────────────────┐
│         Repository Layer                 │
│   (ElectionRepository, etc.)            │
│   - Data access logic                   │
│   - JDBC operations                     │
│   - No business logic                   │
└────────────┬────────────────────────────┘
             │ accesses
             ▼
┌──────────────────────────────────────────┐
│         PostgreSQL Database              │
│   (elections, candidates, students)     │
└──────────────────────────────────────────┘
```

### Component Dependencies
```
Controller → Service Interface → Repository Interface → Database
                ↑                        ↑
                │                        │
         Service Impl              Repository Impl
```

This follows **Dependency Inversion Principle** - high-level modules depend on abstractions.

---

## J. Instructions to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+

### Database Setup

1. **Create Database:**
```bash
psql -U postgres
CREATE DATABASE university_election;
\q
```

2. **Run Schema:**
```bash
psql -U postgres -d university_election -f src/main/resources/schema.sql
```

3. **Update Configuration:**
   Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/university_election
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Build and Run

**Using Maven:**
```bash
# Build project
mvn clean package

# Run application
mvn spring-boot:run
```

**Or run directly:**
```bash
java -jar target/election-api-1.0.0.jar
```

### Verify Installation

1. **Check API is running:**
```bash
curl http://localhost:8080/api/elections
```

2. **View console output** for design pattern demonstrations

3. **Test with Postman or curl**

---

## K. Testing the API

### Using cURL

**Create an Election:**
```bash
curl -X POST http://localhost:8080/api/elections \
  -H "Content-Type: application/json" \
  -d '{
    "name": "University President Election 2027",
    "startDate": "2027-01-10",
    "endDate": "2027-01-19",
    "academicYear": "2026-2027"
  }'
```

**Get All Elections:**
```bash
curl http://localhost:8080/api/elections
```

**Create a Candidate:**
```bash
curl -X POST http://localhost:8080/api/candidates \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bekbolat Aruzhan",
    "faculty": "Software Engineering",
    "yearOfStudy": 2,
    "campaign": "New learning platforms",
    "election": {"id": 1}
  }'
```

**Create a Student:**
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Arguan Bakikair",
    "studentId": "S100",
    "faculty": "Computer Science",
    "yearOfStudy": 3
  }'
```

**Mark Student as Voted:**
```bash
curl -X POST http://localhost:8080/api/students/1/vote
```

### Using Postman

1. Import the API endpoints
2. Set `Content-Type: application/json` for POST/PUT requests
3. Use JSON body for requests
4. Save responses to verify functionality

---

## L. Project Structure

```
election-api/
├── src/
│   └── main/
│       ├── java/com/university/election/
│       │   ├── controller/
│       │   │   ├── ElectionController.java
│       │   │   ├── CandidateController.java
│       │   │   └── StudentController.java
│       │   ├── service/
│       │   │   ├── ElectionService.java
│       │   │   ├── ElectionServiceImpl.java
│       │   │   ├── CandidateService.java
│       │   │   ├── CandidateServiceImpl.java
│       │   │   ├── StudentService.java
│       │   │   └── StudentServiceImpl.java
│       │   ├── repository/
│       │   │   ├── CrudRepository.java
│       │   │   ├── ElectionRepository.java
│       │   │   ├── CandidateRepository.java
│       │   │   └── StudentRepository.java
│       │   ├── model/
│       │   │   ├── BaseEntity.java
│       │   │   ├── Candidate.java
│       │   │   ├── Student.java
│       │   │   ├── Election.java
│       │   │   ├── Validatable.java
│       │   │   └── Votable.java
│       │   ├── patterns/
│       │   │   ├── DatabaseConfig.java (Singleton)
│       │   │   ├── AppLogger.java (Singleton)
│       │   │   ├── EntityFactory.java (Factory)
│       │   │   └── ElectionBuilder.java (Builder)
│       │   ├── utils/
│       │   │   ├── ReflectionUtils.java
│       │   │   └── SortingUtils.java
│       │   ├── exception/
│       │   │   ├── InvalidInputException.java
│       │   │   ├── DuplicateResourceException.java
│       │   │   ├── ResourceNotFoundException.java
│       │   │   ├── DatabaseOperationException.java
│       │   │   └── GlobalExceptionHandler.java
│       │   └── ElectionApplication.java
│       └── resources/
│           ├── application.properties
│           └── schema.sql
├── pom.xml
└── README.md
```

---

## M. Reflection

### What I Learned

1. **Spring Boot Integration**: How to convert a traditional JDBC application into a modern REST API with Spring Boot

2. **Design Patterns in Practice**:
    - Singleton for shared configuration and services
    - Factory for polymorphic object creation
    - Builder for complex object construction

3. **SOLID Principles Application**:
    - How each principle improves code maintainability
    - Why interfaces are crucial for flexibility
    - Dependency injection with Spring

4. **Generics and Type Safety**:
    - Generic repository pattern reduces code duplication
    - Type parameters ensure compile-time safety

5. **Functional Programming with Lambdas**:
    - Stream API for data processing
    - Lambda expressions make code concise
    - Method references for cleaner syntax

6. **Reflection for Runtime Analysis**:
    - Inspect class structure at runtime
    - Useful for debugging and testing
    - Understanding Java type system deeply

### Challenges Faced

1. **Composition Implementation**:
    - Challenge: Ensuring Candidate always has valid Election reference
    - Solution: Validation in service layer, foreign key in database

2. **Generic Repository Design**:
    - Challenge: Making repository work with any entity type
    - Solution: Using Java generics with type parameters <T, ID>

3. **Exception Handling**:
    - Challenge: Consistent error responses across API
    - Solution: Global exception handler with @ControllerAdvice

4. **Spring Boot Configuration**:
    - Challenge: Managing database connection and JDBC template
    - Solution: Spring's auto-configuration and application.properties

### Benefits of SOLID Architecture

**Before (Traditional Approach):**
- Tightly coupled code
- Hard to test
- Changes ripple through codebase
- Difficult to extend

**After (SOLID Approach):**
- **Maintainability**: Changes isolated to specific layers
- **Testability**: Easy to mock interfaces for testing
- **Extensibility**: Add new features without modifying existing code
- **Flexibility**: Swap implementations easily (e.g., switch from JDBC to JPA)

### Value of Design Patterns

**Singleton Pattern:**
- Ensures consistent configuration
- Reduces memory overhead
- Provides global access point

**Factory Pattern:**
- Encapsulates creation logic
- Easy to add new types
- Promotes polymorphism

**Builder Pattern:**
- Readable object construction
- Handles complexity
- Supports optional parameters

### REST API Benefits

**Compared to CLI:**
- Language-agnostic (any client can consume)
- Network-accessible
- Standard HTTP methods
- JSON for easy parsing
- Scalable architecture

**Compared to Traditional Database Access:**
- Abstraction layer protects database
- Business logic enforcement
- Security and validation
- Multiple client support

---

