package ru.akirakozov.sd.refactoring.product.servlet;

import lombok.RequiredArgsConstructor;
import ru.akirakozov.sd.refactoring.product.converters.ProductExtractor;
import ru.akirakozov.sd.refactoring.product.model.Product;
import ru.akirakozov.sd.refactoring.product.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.product.web.ResponseBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
@RequiredArgsConstructor
public class AddProductServlet extends HttpServlet {

    private final ProductRepository productRepository;

    private final ProductExtractor extractor;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Product product = extractor.extract(request);
        productRepository.save(product);

        ResponseBuilder.of(response)
            .withBody("OK")
            .setHeaderUsage(false)
            .build();
    }
}
