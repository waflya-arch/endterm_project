package com.university.election.patterns.singleton;

import java.util.Properties;


public class ElectionConfig {
    private static ElectionConfig instance;
    private final Properties properties;

    // Private constructor prevents instantiation from outside
    private ElectionConfig() {
        properties = new Properties();
        loadDefaultConfig();
    }

    public static ElectionConfig getInstance() {
        if (instance == null) {
            synchronized (ElectionConfig.class) {
                if (instance == null) {
                    instance = new ElectionConfig();
                }
            }
        }
        return instance;
    }

    private void loadDefaultConfig() {
        properties.setProperty("app.name", "University President Election System");
        properties.setProperty("app.version", "1.0.0");
        properties.setProperty("max.candidates.per.election", "10");
        properties.setProperty("min.year.candidate", "2");
        properties.setProperty("max.year.candidate", "4");
        properties.setProperty("min.year.voter", "1");
        properties.setProperty("max.year.voter", "4");
        properties.setProperty("election.voting.enabled", "true");
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public int getMaxCandidatesPerElection() {
        return Integer.parseInt(properties.getProperty("max.candidates.per.election", "10"));
    }

    public int getMinYearForCandidate() {
        return Integer.parseInt(properties.getProperty("min.year.candidate", "2"));
    }

    public int getMaxYearForCandidate() {
        return Integer.parseInt(properties.getProperty("max.year.candidate", "4"));
    }

    public int getMinYearForVoter() {
        return Integer.parseInt(properties.getProperty("min.year.voter", "1"));
    }

    public int getMaxYearForVoter() {
        return Integer.parseInt(properties.getProperty("max.year.voter", "4"));
    }

    public boolean isVotingEnabled() {
        return Boolean.parseBoolean(properties.getProperty("election.voting.enabled", "true"));
    }
}