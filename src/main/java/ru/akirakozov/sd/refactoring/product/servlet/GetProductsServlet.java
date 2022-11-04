package ru.akirakozov.sd.refactoring.product.servlet;

import lombok.RequiredArgsConstructor;
import ru.akirakozov.sd.refactoring.exception.InternalServerError;
import ru.akirakozov.sd.refactoring.product.model.Product;
import ru.akirakozov.sd.refactoring.product.repository.ProductRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * @author akirakozov
 */
@RequiredArgsConstructor
public class GetProductsServlet extends HttpServlet {

    private final ProductRepository productRepository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        List<Product> products = productRepository.findAll();

        try {
            PrintWriter writer = response.getWriter();

            writer.println("<html><body>");
            products.forEach(it -> writer.println(it.getName() + "\t" + it.getPrice() + "</br>"));
            writer.println("</body></html>");
        } catch (IOException e) {
            throw new InternalServerError("Could not write response: " + e.getMessage());
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
