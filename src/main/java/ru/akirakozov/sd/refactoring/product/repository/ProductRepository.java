package ru.akirakozov.sd.refactoring.product.repository;

import ru.akirakozov.sd.refactoring.product.model.Product;
import ru.akirakozov.sd.refactoring.repository.Repository;

public interface ProductRepository extends Repository<Product> {

    Product getProductWithMaxPrice();

    Product getProductWithMinPrice();

    Long getTotalPrice();

    Long countProducts();
}
