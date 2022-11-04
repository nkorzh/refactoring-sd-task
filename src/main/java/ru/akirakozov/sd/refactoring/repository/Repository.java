package ru.akirakozov.sd.refactoring.repository;

import java.util.List;

public interface Repository<T> {
    void save(T t);

    List<T> findAll();
}
