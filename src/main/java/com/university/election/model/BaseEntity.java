package com.university.election.model;

/**
 * Abstract base entity for all domain objects
 * Demonstrates abstraction and Open-Closed Principle (OCP)
 */
public abstract class BaseEntity {
    protected Integer id;
    protected String name;

    public BaseEntity() {
    }

    public BaseEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // Abstract methods that subclasses must implement
    public abstract String getDescription();
    public abstract boolean isEligible();

    // Concrete method available to all subclasses
    public void displayInfo() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Description: " + getDescription());
        System.out.println("Eligible: " + isEligible());
    }

    // Getters and Setters (Encapsulation)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", name='" + name + "'}";
    }
}