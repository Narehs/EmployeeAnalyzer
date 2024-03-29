package org.example.exception;

public class InvalidCsvStructureException extends RuntimeException{
    public InvalidCsvStructureException(String message) {
        super(message);
    }
}
