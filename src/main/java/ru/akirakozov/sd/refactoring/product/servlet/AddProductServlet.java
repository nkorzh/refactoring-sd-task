package ru.akirakozov.sd.refactoring.product.servlet;

import lombok.RequiredArgsConstructor;
import ru.akirakozov.sd.refactoring.exception.InternalServerError;
import ru.akirakozov.sd.refactoring.product.converters.ProductExtractor;
import ru.akirakozov.sd.refactoring.product.model.Product;
import ru.akirakozov.sd.refactoring.product.repository.ProductRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author akirakozov
 */
@RequiredArgsConstructor
public class AddProductServlet extends HttpServlet {

    private final ProductExtractor extractor = new ProductExtractor();

    private final ProductRepository productRepository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Product product = extractor.extract(request);

        productRepository.save(product);
        try {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("OK");
        } catch (IOException e) {
            throw new InternalServerError("Could not write response: " + e.getMessage());
        }
    }
}
