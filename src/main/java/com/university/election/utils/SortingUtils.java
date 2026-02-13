package com.university.election.utils;

import com.university.election.model.Candidate;
import com.university.election.model.Student;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Sorting Utility Class
 * Demonstrates Lambda Expressions and Functional Programming
 */
public class SortingUtils {

    /**
     * Sort students by name using lambda
     */
    public static List<Student> sortStudentsByName(List<Student> students) {
        return students.stream()
                .sorted((s1, s2) -> s1.getName().compareTo(s2.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Sort students by year using lambda
     */
    public static List<Student> sortStudentsByYear(List<Student> students) {
        return students.stream()
                .sorted(Comparator.comparing(Student::getYearOfStudy))
                .collect(Collectors.toList());
    }

    /**
     * Filter students who haven't voted using lambda
     */
    public static List<Student> filterStudentsNotVoted(List<Student> students) {
        return students.stream()
                .filter(s -> !s.getHasVoted())
                .collect(Collectors.toList());
    }

    /**
     * Sort candidates by name using lambda
     */
    public static List<Candidate> sortCandidatesByName(List<Candidate> candidates) {
        return candidates.stream()
                .sorted((c1, c2) -> c1.getName().compareTo(c2.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Filter eligible candidates using lambda
     */
    public static List<Candidate> filterEligibleCandidates(List<Candidate> candidates) {
        return candidates.stream()
                .filter(Candidate::isEligible)
                .collect(Collectors.toList());
    }

    /**
     * Group students by faculty using lambda
     */
    public static java.util.Map<String, List<Student>> groupStudentsByFaculty(List<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getFaculty));
    }

    /**
     * Count students who voted using lambda
     */
    public static long countVotedStudents(List<Student> students) {
        return students.stream()
                .filter(Student::getHasVoted)
                .count();
    }

    /**
     * Demonstrate lambda usage
     */
    public static void demonstrateLambdas(List<Student> students) {
        System.out.println("=== Lambda Expression Demonstration ===");

        // Using forEach with lambda
        System.out.println("\nAll students:");
        students.forEach(s -> System.out.println("  " + s.getName()));

        // Using filter and count
        long votedCount = students.stream()
                .filter(Student::getHasVoted)
                .count();
        System.out.println("\nStudents who voted: " + votedCount);

        // Using map and collect
        List<String> names = students.stream()
                .map(Student::getName)
                .collect(Collectors.toList());
        System.out.println("Student names: " + names);

        // Using sorted with custom comparator
        System.out.println("\nStudents sorted by year:");
        students.stream()
                .sorted(Comparator.comparing(Student::getYearOfStudy))
                .forEach(s -> System.out.println("  Year " + s.getYearOfStudy() + ": " + s.getName()));

        System.out.println("========================================\n");
    }
}