package com.university.election.model.interfaces;

/**
 * Interface for entities that can be validated
 * Demonstrates Interface Segregation Principle (ISP)
 */
public interface Validatable {
    /**
     * Validate the entity
     * @return true if valid, false otherwise
     */
    boolean validate();

    /**
     * Default method to get validation message
     * Demonstrates default methods in interfaces
     */
    default String getValidationMessage() {
        return validate() ? "Valid" : "Invalid entity data";
    }

    /**
     * Static utility method for common validation
     * Demonstrates static methods in interfaces
     */
    static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
}