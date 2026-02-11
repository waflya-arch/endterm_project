package com.university.election.patterns.singleton;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    private static DatabaseConfig instance;
    private DataSource dataSource;
    private String url;
    private String username;
    private String password;

    // Private constructor
    private DatabaseConfig() {
        // Default configuration (will be overridden by Spring)
        this.url = "jdbc:postgresql://localhost:5434/waflya";
        this.username = "postgres";
        this.password = "waflya";
    }

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

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Connection getConnection() throws SQLException {
        if (dataSource != null) {
            return dataSource.getConnection();
        }
        throw new SQLException("DataSource not configured. Please ensure Spring Boot has initialized the database.");
    }

    // Getters and setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConnectionInfo() {
        return String.format("Database: %s, User: %s", url, username);
    }
}