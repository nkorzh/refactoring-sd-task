package ru.akirakozov.sd.refactoring.utils;

import java.sql.SQLException;
import java.sql.Statement;

@FunctionalInterface
public interface StatementOperation {
    void execute(Statement t) throws SQLException;
}
