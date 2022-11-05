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
import ru.akirakozov.sd.refactoring.repository.utils.DbUtils;
import ru.akirakozov.sd.refactoring.utils.TextUtils;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // should be retrieved from Consul with credits from Vault
        String dbUrl = "jdbc:sqlite:test.db";

        try (Connection c = DriverManager.getConnection(dbUrl)) {
            String initScript = TextUtils.getResourceAsString("sql/init.sql");
            DbUtils.query(c, initScript);

            Server server = new Server(8080);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);

            ProductRepository productRepository = new ProductRepositoryImpl(c);
            ProductMapper productMapper = new ProductMapperImpl();

            addServlet("/add-product", new AddProductServlet(productRepository, new ProductExtractor()), context);
            addServlet("/get-products", new GetProductsServlet(productRepository, productMapper), context);
            addServlet("/query", new QueryServlet(productRepository, productMapper), context);

            server.start();
            server.join();
        }
    }

    private static void addServlet(String path, HttpServlet servlet, ServletContextHandler context) {
        context.addServlet(new ServletHolder(servlet), path);
    }
}
