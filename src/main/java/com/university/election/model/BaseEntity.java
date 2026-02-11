package com.university.election.model;

public abstract class BaseEntity {
    protected Integer id;
    protected String name;

    public BaseEntity() {
    }

    public BaseEntity(String name) {
        this.name = name;
    }

    public BaseEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // Abstract methods to be implemented by subclasses
    public abstract String getDescription();
    public abstract boolean isEligible();
    public abstract String getEntityType();

    public void displayInfo() {
        System.out.println("=================================");
        System.out.println("Entity Type: " + getEntityType());
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Description: " + getDescription());
        System.out.println("Eligible: " + (isEligible() ? "Yes" : "No"));
        System.out.println("=================================");
    }

    // Getters and Setters
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
        return String.format("%s[id=%d, name='%s']", getEntityType(), id, name);
    }
}