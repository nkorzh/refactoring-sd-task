package ru.akirakozov.sd.refactoring.repository.utils;

import java.sql.SQLException;
import java.sql.Statement;

@FunctionalInterface
public interface QueryOperation<T> {
    T execute(Statement t) throws SQLException;
}
