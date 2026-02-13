package com.university.election;

import com.university.election.model.Candidate;
import com.university.election.model.Election;
import com.university.election.model.Student;
import com.university.election.patterns.singleton.AppLogger;
import com.university.election.patterns.singleton.DatabaseConfig;
import com.university.election.patterns.builder.ElectionBuilder;
import com.university.election.patterns.factory.EntityFactory;
import com.university.election.utils.ReflectionUtils;
import com.university.election.utils.SortingUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


 // Main Spring Boot Application
 // University President Election Management System
@SpringBootApplication
public class ElectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElectionApplication.class, args);
	}

	// CommandLineRunner to demonstrate design patterns and OOP features
	@Bean
	public CommandLineRunner demo(DatabaseConfig dbConfig, EntityFactory factory, AppLogger logger) {
		return args -> {
			System.out.println("\n" + "=".repeat(60));
			System.out.println("  UNIVERSITY PRESIDENT ELECTION MANAGEMENT SYSTEM");
			System.out.println("  Spring Boot REST API - Endterm Project");
			System.out.println("=".repeat(60) + "\n");

			// ========== SINGLETON PATTERN DEMONSTRATION ==========
			logger.info("Starting Design Pattern Demonstrations...");

			dbConfig.displayConfiguration();

			// Verify singleton instance
			DatabaseConfig dbConfig2 = DatabaseConfig.getInstance();
			System.out.println("Same instance? " + (dbConfig == dbConfig2));
			System.out.println();

			// ========== BUILDER PATTERN DEMONSTRATION ==========
			ElectionBuilder.demonstrateBuilder();

			// ========== FACTORY PATTERN DEMONSTRATION ==========
			factory.demonstrateFactory();
			System.out.println();

			// ========== REFLECTION DEMONSTRATION ==========
			Election testElection = ElectionBuilder.builder()
					.withId(1)
					.withName("Test Election")
					.withDateRange(LocalDate.now(), LocalDate.now().plusDays(7))
					.withAcademicYear("2025-2026")
					.build();

			Candidate testCandidate = new Candidate(1, "Serik Akmetovich", "CS", 3, "Better campus!", testElection);
			Student testStudent = new Student(1, "Andrey Plovskiy", "S001", "SE", 2, false);

			ReflectionUtils.printClassInfo(testCandidate);
			ReflectionUtils.printClassInfo(testStudent);

			// ========== LAMBDA EXPRESSIONS DEMONSTRATION ==========
			List<Student> sampleStudents = Arrays.asList(
					new Student(1, "Arman Ertay", "S001", "CS", 3, true),
					new Student(2, "Donald Trump", "S002", "SE", 1, false),
					new Student(3, "Charlie Kirk", "S003", "CS", 4, false),
					new Student(4, "Jordan Barret", "S004", "SE", 2, true)
			);

			SortingUtils.demonstrateLambdas(sampleStudents);

			// ========== OOP PRINCIPLES DEMONSTRATION ==========
			logger.info("Demonstrating OOP Principles...");

			// Polymorphism
			System.out.println("\n=== Polymorphism ===");
			com.university.election.model.BaseEntity entity1 = testCandidate;
			com.university.election.model.BaseEntity entity2 = testStudent;

			entity1.displayInfo();
			System.out.println();
			entity2.displayInfo();
			System.out.println();

			// Interface methods
			System.out.println("=== Interface Methods ===");
			System.out.println("Candidate validation: " + testCandidate.validate());
			System.out.println("Candidate message: " + testCandidate.getValidationMessage());
			System.out.println("Student can vote: " + testStudent.canVote());
			System.out.println("Student voting status: " + testStudent.getVotingStatus());
			System.out.println();

			logger.info("All demonstrations completed successfully!");
			logger.info("REST API is now ready at http://localhost:8080");
			logger.info("API Endpoints:");
			logger.info("  - GET    /api/elections");
			logger.info("  - POST   /api/elections");
			logger.info("  - GET    /api/candidates");
			logger.info("  - POST   /api/candidates");
			logger.info("  - GET    /api/students");
			logger.info("  - POST   /api/students");
			logger.info("  - POST   /api/students/{id}/vote");
			System.out.println("\n" + "=".repeat(60) + "\n");
		};
	}
}