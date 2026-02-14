package com.university.election.patterns.singleton;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Purpose: Store frequently accessed data in memory to avoid repeated database queries
 *
 * Features:
 * - Thread-safe using ConcurrentHashMap
 * - Singleton instance across application
 * - Manual and automatic cache invalidation
 * - Generic key-value storage
 *
 * Example Usage:
 *   CacheManager cache = CacheManager.getInstance();
 *   cache.put("elections_all", electionsList);
 *   List<Election> cached = cache.get("elections_all");
 */
@Component
public class CacheManager {

    // Single instance (Singleton)
    private static CacheManager instance;

    // In-memory storage - thread-safe Map
    private final Map<String, Object> cache;

    // Private constructor prevents external instantiation
    private CacheManager() {
        this.cache = new ConcurrentHashMap<>();
        System.out.println("✅ CacheManager initialized (Singleton)");
    }

    /**
     * Get singleton instance (thread-safe)
     */
    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    /**
     * Put data in cache
     * @param key Unique identifier
     * @param value Data to cache
     */
    public void put(String key, Object value) {
        cache.put(key, value);
        System.out.println("📦 Cached: " + key);
    }

    /**
     * Get data from cache
     * @param key Unique identifier
     * @return Cached data or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        Object value = cache.get(key);
        if (value != null) {
            System.out.println("⚡ Cache HIT: " + key);
        } else {
            System.out.println("❌ Cache MISS: " + key);
        }
        return (T) value;
    }

    /**
     * Check if key exists in cache
     */
    public boolean contains(String key) {
        return cache.containsKey(key);
    }

    /**
     * Remove specific key from cache
     */
    public void invalidate(String key) {
        cache.remove(key);
        System.out.println("🗑️  Cache invalidated: " + key);
    }

    /**
     * Remove all keys matching pattern
     * Example: invalidatePattern("elections_") removes all election caches
     */
    public void invalidatePattern(String pattern) {
        cache.keySet().removeIf(key -> key.startsWith(pattern));
        System.out.println("🗑️  Cache pattern invalidated: " + pattern + "*");
    }

    /**
     * Clear entire cache
     */
    public void clear() {
        cache.clear();
        System.out.println("🗑️  Cache cleared completely");
    }

    /**
     * Get cache statistics
     */
    public void printStats() {
        System.out.println("📊 Cache Statistics:");
        System.out.println("   Total entries: " + cache.size());
        System.out.println("   Keys: " + cache.keySet());
    }

    /**
     * Get cache size
     */
    public int size() {
        return cache.size();
    }
}