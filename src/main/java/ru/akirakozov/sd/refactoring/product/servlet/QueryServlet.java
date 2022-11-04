package ru.akirakozov.sd.refactoring.product.servlet;

import lombok.RequiredArgsConstructor;
import ru.akirakozov.sd.refactoring.product.model.Product;
import ru.akirakozov.sd.refactoring.product.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.product.web.ProductMapper;
import ru.akirakozov.sd.refactoring.product.web.ResponseBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
@RequiredArgsConstructor
public class QueryServlet extends HttpServlet {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            Product product = productRepository.getProductWithMaxPrice();

            ResponseBuilder.of(response)
                .withHeader("Product with max price: ")
                .withBody(productMapper.map(product))
                .build();
        } else if ("min".equals(command)) {
            Product product = productRepository.getProductWithMinPrice();

            ResponseBuilder.of(response)
                .withHeader("Product with min price: ")
                .withBody(productMapper.map(product))
                .build();
        } else if ("sum".equals(command)) {
            Long sumPrice = productRepository.getTotalPrice();
            ResponseBuilder.of(response)
                .withBody(String.format("Summary price: %n%s", sumPrice))
                .build();
        } else if ("count".equals(command)) {
            Long count = productRepository.getProductCount();

            ResponseBuilder.of(response)
                .withBody(String.format("Number of products: %n%s", count))
                .build();
        } else {
            ResponseBuilder.of(response)
                .withError("Unknown command: " + command)
                .build();
        }
    }
}
