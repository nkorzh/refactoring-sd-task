package ru.akirakozov.sd.refactoring.product.servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.product.repository.ProductRepositoryImpl;
import ru.akirakozov.sd.refactoring.utils.TestDbUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.akirakozov.sd.refactoring.utils.HtmlUtils.LINE_BREAK;

class AddProductServletTest {

    private StringWriter stringWriter;
    private PrintWriter writer;
    private Connection connection;
    private AddProductServlet servlet;

    @BeforeEach
    void setUp() throws SQLException {
        TestDbUtils.initTestDb();
        TestDbUtils.executeInTestDb(stmt ->
            stmt.executeUpdate("DELETE FROM PRODUCT;"));

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        connection = DriverManager.getConnection("jdbc:sqlite:test.db");
        servlet = new AddProductServlet(new ProductRepositoryImpl(connection));
    }

    @AfterEach
    void clearWriter() throws SQLException {
        writer.close();
        connection.close();
    }

    @Test
    void shouldCreateNewProduct() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(eq("name"))).thenReturn("Varenik");
        when(request.getParameter(eq("price"))).thenReturn("300");

        servlet.doGet(request, response);

        writer.flush();

        assertThat(stringWriter.toString(), equalTo("OK" + LINE_BREAK));
        TestDbUtils.executeInTestDb(stmt -> {
            ResultSet rs = stmt.executeQuery("SELECT NAME, PRICE FROM PRODUCT;");
            rs.next();

            assertThat(rs.getString("name"), equalTo("Varenik"));
            assertThat(rs.getString("price"), equalTo("300"));
            assertThat(rs.next(), equalTo(false));
        });
    }

    @Test
    void shouldNotCreateNewProductWithNullPrice() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(eq("name"))).thenReturn("Varenik");
        when(request.getParameter(eq("price"))).thenReturn(null);

        assertThrows(RuntimeException.class, () ->
            servlet.doGet(request, response)
        );
        writer.flush();

        assertThat(stringWriter.toString(), emptyString());
        TestDbUtils.executeInTestDb(stmt -> {
            ResultSet rs = stmt.executeQuery("SELECT NAME, PRICE FROM PRODUCT;");

            assertThat(rs.next(), equalTo(false));
        });
    }

    @Test
    void shouldCreateNewProductWithNullName() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(eq("name"))).thenReturn(null);
        when(request.getParameter(eq("price"))).thenReturn("350");

        assertDoesNotThrow(() -> servlet.doGet(request, response));
        writer.flush();

        assertThat(stringWriter.toString(), equalTo("OK" + LINE_BREAK));
        TestDbUtils.executeInTestDb(stmt -> {
            ResultSet rs = stmt.executeQuery("SELECT NAME, PRICE FROM PRODUCT;");
            rs.next();

            assertThat(rs.getString("name"), equalTo("null"));
            assertThat(rs.getString("price"), equalTo("350"));
            assertThat(rs.next(), equalTo(false));
        });
    }
}
