package com.university.election.patterns.singleton;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Singleton Pattern: Database Configuration Manager
 * Ensures single instance of database configuration throughout the application
 */
@Component
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private static DatabaseConfig instance;

    // Private constructor prevents external instantiation
    public DatabaseConfig() {
        if (instance != null) {
            throw new IllegalStateException("DatabaseConfig instance already exists!");
        }
        instance = this;
    }

    /**
     * Get singleton instance
     */
    public static DatabaseConfig getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfig.class) {
                if (instance == null) {
                    instance = new DatabaseConfig();
                }
            }
        }
        return instance;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void displayConfiguration() {
        System.out.println("=== Database Configuration (Singleton) ===");
        System.out.println("URL: " + url);
        System.out.println("Username: " + username);
        System.out.println("Instance: " + this.hashCode());
    }
}