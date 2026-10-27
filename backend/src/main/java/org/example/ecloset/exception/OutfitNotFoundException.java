package org.example.ecloset.exception;

public class OutfitNotFoundException extends RuntimeException {
    public OutfitNotFoundException(String message) {
        super(message);
    }
}
