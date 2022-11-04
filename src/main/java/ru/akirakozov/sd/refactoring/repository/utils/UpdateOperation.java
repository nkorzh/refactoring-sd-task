package ru.akirakozov.sd.refactoring.repository.utils;

import java.sql.SQLException;
import java.sql.Statement;

@FunctionalInterface
public interface UpdateOperation {

    void update(Statement t) throws SQLException;
}
