package ru.akirakozov.sd.refactoring.exception;

public class InternalServerError extends RuntimeException {
    public InternalServerError(String message) {
        super(message);
    }
}
