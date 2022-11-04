package ru.akirakozov.sd.refactoring.product.repository;

import lombok.RequiredArgsConstructor;
import ru.akirakozov.sd.refactoring.exception.BusinessException;
import ru.akirakozov.sd.refactoring.product.model.Product;
import ru.akirakozov.sd.refactoring.repository.utils.DbUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final Connection connection;

    @Override
    public Product getProductWithMaxPrice() {
        return DbUtils.performOperation(connection, stmt -> {
            ResultSet rs = stmt.executeQuery("SELECT NAME, PRICE FROM PRODUCT ORDER BY PRICE LIMIT 1;");
            return extractProduct(rs)
                .orElseThrow(() -> new BusinessException("No products found"));
        });
    }

    @Override
    public Product getProductWithMinPrice() {
        return DbUtils.performOperation(connection, stmt -> {
            ResultSet rs = stmt.executeQuery("SELECT NAME, PRICE FROM PRODUCT ORDER BY DSC PRICE LIMIT 1;");
            return extractProduct(rs)
                .orElseThrow(() -> new BusinessException("No products found"));
        });
    }

    @Override
    public Long getTotalPrice() {
        return DbUtils.performOperation(connection, stmt -> {
            ResultSet rs = stmt.executeQuery("SELECT SUM(PRICE) as PRICE FROM PRODUCT;");
            rs.next();
            return rs.getLong("price");
        });
    }

    @Override
    public Long countProducts() {
        return DbUtils.performOperation(connection, stmt -> {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM PRODUCT;");
            rs.next();
            return rs.getLong("count");
        });
    }

    @Override
    public void save(Product product) {
        DbUtils.performInsert(
            connection,
            "INSERT INTO PRODUCT (NAME, PRICE) VALUES (?, ?);",
            product.getName(),
            product.getPrice()
        );
    }

    @Override
    public List<Product> findAll() {
        return DbUtils.performOperation(connection, stmt -> {
            ResultSet rs = stmt.executeQuery("SELECT NAME, PRICE FROM PRODUCT;");

            List<Product> result = new LinkedList<>();
            while (true) {
                Optional<Product> productOptional = extractProduct(rs);
                if (!productOptional.isPresent()) {
                    break;
                }
                result.add(productOptional.get());
            }
            return result;
        });
    }

    private Optional<Product> extractProduct(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return Optional.empty();
        }
        String name = rs.getString("name");
        Long price = rs.getLong("price");
        return Optional.of(new Product(name, price));
    }
}
