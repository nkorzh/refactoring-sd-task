package ru.akirakozov.sd.refactoring.product.servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.product.repository.ProductRepositoryImpl;
import ru.akirakozov.sd.refactoring.product.web.ProductMapperImpl;
import ru.akirakozov.sd.refactoring.utils.TestDbUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.akirakozov.sd.refactoring.product.web.HtmlUtils.HTML_BREAK;
import static ru.akirakozov.sd.refactoring.product.web.HtmlUtils.HTML_END;
import static ru.akirakozov.sd.refactoring.product.web.HtmlUtils.HTML_HEADER;
import static ru.akirakozov.sd.refactoring.product.web.HtmlUtils.LINE_BREAK;
import static ru.akirakozov.sd.refactoring.product.web.HtmlUtils.TAB;
import static ru.akirakozov.sd.refactoring.utils.TestDbUtils.TEST_DB_URL;

class GetProductsServletTest {

    private StringWriter stringWriter;
    private PrintWriter writer;
    private Connection connection;
    private GetProductsServlet servlet;


    @BeforeEach
    void setUp() throws SQLException {
        TestDbUtils.initTestDb();
        TestDbUtils.executeInTestDb(stmt ->
            stmt.executeUpdate("DELETE FROM PRODUCT;"));

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        connection = DriverManager.getConnection(TEST_DB_URL);
        servlet = new GetProductsServlet(new ProductRepositoryImpl(connection), new ProductMapperImpl());
    }

    @AfterEach
    void clearWriter() throws SQLException {
        writer.close();
        connection.close();
    }

    @Test
    void shouldGetProducts() throws IOException {
        TestDbUtils.executeInTestDb(stmt ->
            stmt.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES ('IPhone', 300), ('Varenik', 350);"));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        assertThat(
            stringWriter.toString(),
            equalTo(HTML_HEADER + LINE_BREAK +
                    "IPhone" + TAB + "300" + HTML_BREAK + LINE_BREAK +
                    "Varenik" + TAB + "350" + HTML_BREAK + LINE_BREAK +
                    HTML_END + LINE_BREAK
            )
        );
    }
}
