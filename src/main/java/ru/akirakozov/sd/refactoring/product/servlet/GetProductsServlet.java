package ru.akirakozov.sd.refactoring.product.servlet;

import lombok.RequiredArgsConstructor;
import ru.akirakozov.sd.refactoring.product.model.Product;
import ru.akirakozov.sd.refactoring.product.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.product.web.ProductMapper;
import ru.akirakozov.sd.refactoring.product.web.ResponseBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author akirakozov
 */
@RequiredArgsConstructor
public class GetProductsServlet extends HttpServlet {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        List<Product> products = productRepository.findAll();
        ResponseBuilder.of(response)
            .withBody(productMapper.map(products))
            .build();
    }
}
