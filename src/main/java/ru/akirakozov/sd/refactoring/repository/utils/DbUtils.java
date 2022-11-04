package ru.akirakozov.sd.refactoring.repository.utils;

import ru.akirakozov.sd.refactoring.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtils {

    public static <T> T performOperation(Connection connection, QueryOperation<T> stmtOperation) {
        try {
            Statement stmt = connection.createStatement();
            T result = stmtOperation.execute(stmt);
            stmt.close();
            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public static void performInsert(Connection connection, String sql, Object... args) {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                stmt.setObject(i + 1, args[i]);
            }
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
