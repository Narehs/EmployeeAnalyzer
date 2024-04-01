package org.company.exception;

public class InvalidCsvDataException extends RuntimeException {
    public InvalidCsvDataException(String message) {
        super(message);
    }
}
