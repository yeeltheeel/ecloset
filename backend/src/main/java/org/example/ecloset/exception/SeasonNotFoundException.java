package org.example.ecloset.exception;

public class SeasonNotFoundException extends RuntimeException {
    public SeasonNotFoundException(String message) {
        super(message);
    }
}