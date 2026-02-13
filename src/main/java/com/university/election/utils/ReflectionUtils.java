package com.university.election.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reflection Utility Class
 * Demonstrates Runtime Type Identification (RTTI) and Reflection
 */
public class ReflectionUtils {

    /**
     * Get class name
     */
    public static String getClassName(Object obj) {
        return obj.getClass().getSimpleName();
    }

    /**
     * Get full class name with package
     */
    public static String getFullClassName(Object obj) {
        return obj.getClass().getName();
    }

    /**
     * Get all fields of a class
     */
    public static List<String> getFields(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        return Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());
    }

    /**
     * Get all methods of a class
     */
    public static List<String> getMethods(Object obj) {
        Method[] methods = obj.getClass().getDeclaredMethods();
        return Arrays.stream(methods)
                .map(Method::getName)
                .collect(Collectors.toList());
    }

    /**
     * Get field value using reflection
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Print complete class information
     */
    public static void printClassInfo(Object obj) {
        System.out.println("=== Reflection Analysis ===");
        System.out.println("Class: " + getClassName(obj));
        System.out.println("Package: " + obj.getClass().getPackage().getName());
        System.out.println("Full Name: " + getFullClassName(obj));
        System.out.println("Superclass: " + obj.getClass().getSuperclass().getSimpleName());

        System.out.println("\nInterfaces:");
        Arrays.stream(obj.getClass().getInterfaces())
                .forEach(i -> System.out.println("  - " + i.getSimpleName()));

        System.out.println("\nFields:");
        getFields(obj).forEach(f -> System.out.println("  - " + f));

        System.out.println("\nMethods:");
        getMethods(obj).forEach(m -> System.out.println("  - " + m));

        System.out.println("========================\n");
    }
}