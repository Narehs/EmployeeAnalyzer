package org.company.exception;

public class CsvIOException extends RuntimeException {
    public CsvIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
