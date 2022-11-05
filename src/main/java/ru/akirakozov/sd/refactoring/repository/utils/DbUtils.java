package ru.akirakozov.sd.refactoring.repository.utils;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import ru.akirakozov.sd.refactoring.exception.DataAccessException;
import ru.akirakozov.sd.refactoring.repository.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class DbUtils {

    public static <T> @NonNull List<T> query(Connection connection, @NonNull String sql, RowMapper<T> rowMapper) {
        return result(connection, sql, rs -> {
            List<T> result = new LinkedList<>();
            while (rs.next()) {
                T object = rowMapper.mapRow(rs);
                if (Objects.isNull(object)) {
                    break;
                }
                result.add(object);
            }
            return result;
        });
    }

    public static <T> @Nullable T queryForObject(Connection connection, @NonNull String sql, RowMapper<T> rowMapper) {
        return result(connection, sql, rs -> {
            rs.next();
            return rowMapper.mapRow(rs);
        });
    }

    private static <T, V> @NonNull V result(Connection connection,
                                            @NonNull String sql,
                                            ResultSetExtractor<V> rsExtractor) {
        try {
            Statement stmt = null;
            try {
                stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                return rsExtractor.extract(rs);
            } catch (SQLException e) {
                throw new DataAccessException(e);
            } finally {
                if (Objects.nonNull(stmt)) {
                    stmt.close();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public static void query(Connection connection, @NonNull String sql, Object... args) {
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

    @FunctionalInterface
    interface ResultSetExtractor<V> {
        V extract(ResultSet rs) throws SQLException;
    }
}
