package ru.akirakozov.sd.refactoring.product.web;

import ru.akirakozov.sd.refactoring.product.model.Product;

import java.util.List;

public interface ProductMapper {
    String map(Product product);

    String map(List<Product> products);
}
