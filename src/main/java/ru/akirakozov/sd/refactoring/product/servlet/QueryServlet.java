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

/**
 * @author akirakozov
 */
@RequiredArgsConstructor
public class QueryServlet extends HttpServlet {

    private final ProductRepository productRepository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String command = request.getParameter("command");

        try {
            PrintWriter writer = response.getWriter();
            if ("max".equals(command)) {
                Product product = productRepository.getProductWithMaxPrice();

                writer.println("<html><body>");
                writer.println("<h1>Product with max price: </h1>");
                writer.println(product.getName() + "\t" + product.getPrice() + "</br>");
                writer.println("</body></html>");
            } else if ("min".equals(command)) {
                Product product = productRepository.getProductWithMinPrice();

                writer.println("<html><body>");
                writer.println("<h1>Product with min price: </h1>");
                writer.println(product.getName() + "\t" + product.getPrice() + "</br>");
                writer.println("</body></html>");
            } else if ("sum".equals(command)) {
                Long sumPrice = productRepository.getTotalPrice();

                writer.println("<html><body>");
                writer.println("Summary price: ");
                writer.println(sumPrice);
                writer.println("</body></html>");
            } else if ("count".equals(command)) {
                Long count = productRepository.getProductCount();

                writer.println("<html><body>");
                writer.println("Number of products: ");
                writer.println(count);
                writer.println("</body></html>");
            } else {
                response.getWriter().println("Unknown command: " + command);
            }
        } catch (IOException e) {
            throw new InternalServerError("Could not write response: " + e.getMessage());
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
