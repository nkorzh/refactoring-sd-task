package ru.akirakozov.sd.refactoring.product.converters;

import javax.servlet.http.HttpServletRequest;

public interface RequestExtractor<T> {
    T extract(HttpServletRequest request);
}
