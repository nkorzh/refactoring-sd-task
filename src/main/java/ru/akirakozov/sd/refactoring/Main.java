package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.product.converters.ProductExtractor;
import ru.akirakozov.sd.refactoring.product.repository.ProductRepository;
import ru.akirakozov.sd.refactoring.product.repository.ProductRepositoryImpl;
import ru.akirakozov.sd.refactoring.product.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.product.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.product.servlet.QueryServlet;
import ru.akirakozov.sd.refactoring.product.web.ProductMapper;
import ru.akirakozov.sd.refactoring.product.web.ProductMapperImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                         "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                         " NAME           TEXT    NOT NULL, " +
                         " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();

            Server server = new Server(8080);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);

            ProductRepository productRepository = new ProductRepositoryImpl(c);
            ProductMapper productMapper = new ProductMapperImpl();

            context.addServlet(new ServletHolder(new AddProductServlet(productRepository, new ProductExtractor())), "/add-product");
            context.addServlet(new ServletHolder(new GetProductsServlet(productRepository, productMapper)), "/get-products");
            context.addServlet(new ServletHolder(new QueryServlet(productRepository, productMapper)), "/query");

            server.start();
            server.join();

        }
    }
}
