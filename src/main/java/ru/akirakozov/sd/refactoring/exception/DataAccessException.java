package ru.akirakozov.sd.refactoring.exception;

public class DataAccessException extends RuntimeException {
    public DataAccessException(Exception nested) {
        super(nested);
    }

    public DataAccessException(String message) {
        super(message);
    }
}
