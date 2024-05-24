package com.example.workflow.utils;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class Checkers {

    public static <T> T mustNotBeNull(T value, String message) {
        return ofNullable(value).orElseThrow(() -> new IllegalArgumentException(message));
    }

    public static <T> T mustNotBeEmpty(Optional<T> optional, String message) {
        return optional.orElseThrow(() -> new IllegalArgumentException(message));
    }

    public static String mustNotBeBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}
