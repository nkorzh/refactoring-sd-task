package ru.akirakozov.sd.refactoring.product.repository;

import lombok.RequiredArgsConstructor;
import ru.akirakozov.sd.refactoring.exception.BusinessException;
import ru.akirakozov.sd.refactoring.product.model.Product;
import ru.akirakozov.sd.refactoring.repository.utils.DbUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    // should be a connection pool
    private final Connection connection;

    @Override
    public Product getProductWithMaxPrice() {
        Product product = DbUtils.queryForObject(
            connection,
            "SELECT NAME, PRICE FROM PRODUCT ORDER BY PRICE DESC LIMIT 1;",
            this::productMapper
        );
        return Optional.ofNullable(product)
            .orElseThrow(() -> new BusinessException("No products found"));
    }

    @Override
    public Product getProductWithMinPrice() {
        Product product = DbUtils.queryForObject(
            connection,
            "SELECT NAME, PRICE FROM PRODUCT ORDER BY PRICE ASC LIMIT 1;",
            this::productMapper
        );
        return Optional.ofNullable(product)
            .orElseThrow(() -> new BusinessException("No products found"));
    }

    @Override
    public Long getTotalPrice() {
        return DbUtils.queryForObject(
            connection,
            "SELECT SUM(PRICE) as PRICE FROM PRODUCT;",
            rs -> rs.getLong("price")
        );
    }

    @Override
    public Long getProductCount() {
        return DbUtils.queryForObject(
            connection,
            "SELECT COUNT(*) as count FROM PRODUCT;",
            rs -> rs.getLong("count")
        );
    }

    @Override
    public void save(Product product) {
        DbUtils.query(
            connection,
            "INSERT INTO PRODUCT (NAME, PRICE) VALUES (?, ?);",
            product.getName(),
            product.getPrice()
        );
    }

    @Override
    public List<Product> findAll() {
        return DbUtils.query(connection, "SELECT NAME, PRICE FROM PRODUCT;", this::productMapper);
    }

    private Product productMapper(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        Long price = rs.getLong("price");
        return new Product(name, price);
    }
}
