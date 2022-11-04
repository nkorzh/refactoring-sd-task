package ru.akirakozov.sd.refactoring.product.converters;

import ru.akirakozov.sd.refactoring.exception.InvalidRequestException;
import ru.akirakozov.sd.refactoring.product.model.Product;

import javax.servlet.http.HttpServletRequest;

public class ProductExtractor implements RequestExtractor<Product> {

    @Override
    public Product extract(HttpServletRequest request) {
        String name = request.getParameter("name");
        if (name == null) {
            name = "null"; // default behaviour allowed nulls
        }
        long price;
        try {
            price = Long.parseLong(request.getParameter("price"));
        } catch (NumberFormatException e) {
            throw new InvalidRequestException("Product price should be a number!");
        }
        return new Product(name, price);
    }
}
