package ru.akirakozov.sd.refactoring.repository;

import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {
    @Nullable
    T mapRow(ResultSet rs) throws SQLException;
}
