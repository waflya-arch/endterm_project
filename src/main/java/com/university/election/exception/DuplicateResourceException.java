package com.university.election.exception;

/**
 * Exception for duplicate resources (e.g., duplicate student ID)
 */
public class DuplicateResourceException extends InvalidInputException {
    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}