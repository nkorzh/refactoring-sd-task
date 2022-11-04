package ru.akirakozov.sd.refactoring.utils;

import ru.akirakozov.sd.refactoring.repository.utils.UpdateOperation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TestDbUtils {

    public static void initTestDb() {
        executeInTestDb(
            stmt -> stmt.executeUpdate("CREATE TABLE IF NOT EXISTS PRODUCT" +
                                       "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                       " NAME           TEXT    NOT NULL, " +
                                       " PRICE          INT     NOT NULL);")
        );
    }

    public static void executeInTestDb(UpdateOperation stmtOperation) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            stmtOperation.update(stmt);
            stmt.close();
        } catch (Exception e) {
            System.err.printf(
                "Error: %s%n%s%n",
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                    .map(Object::toString)
                    .collect(Collectors.joining(String.format("%n")))
            );
        }
    }
}
