package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.akirakozov.sd.refactoring.utils.TestDbUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.akirakozov.sd.refactoring.utils.HtmlUtils.HEADER_END;
import static ru.akirakozov.sd.refactoring.utils.HtmlUtils.HEADER_START;
import static ru.akirakozov.sd.refactoring.utils.HtmlUtils.HTML_BREAK;
import static ru.akirakozov.sd.refactoring.utils.HtmlUtils.HTML_END;
import static ru.akirakozov.sd.refactoring.utils.HtmlUtils.HTML_HEADER;
import static ru.akirakozov.sd.refactoring.utils.HtmlUtils.LINE_BREAK;
import static ru.akirakozov.sd.refactoring.utils.HtmlUtils.TAB;

class QueryServletTest {

    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() {
        TestDbUtils.initTestDb();
        TestDbUtils.executeInTestDb(stmt -> stmt.executeUpdate("DELETE FROM PRODUCT;"));
        TestDbUtils.executeInTestDb(stmt ->
            stmt.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES ('IPhone', 300), ('Varenik', 350);"));

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
    }

    @AfterEach
    void clearWriter() {
        writer.close();
    }

    @Test
    void shouldReturnMax() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(eq("command"))).thenReturn("max");

        when(response.getWriter()).thenReturn(writer);

        new QueryServlet().doGet(request, response);

        writer.flush();
        assertThat(
            stringWriter.toString(),
            equalTo(HTML_HEADER + LINE_BREAK +
                    HEADER_START + "Product with max price: " + HEADER_END + LINE_BREAK +
                    "Varenik" + TAB + "350" + HTML_BREAK + LINE_BREAK +
                    HTML_END + LINE_BREAK
            )
        );
    }

    @Test
    void shouldReturnMin() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(eq("command"))).thenReturn("min");

        when(response.getWriter()).thenReturn(writer);

        new QueryServlet().doGet(request, response);

        writer.flush();
        assertThat(
            stringWriter.toString(),
            equalTo(HTML_HEADER + LINE_BREAK +
                    HEADER_START + "Product with min price: " + HEADER_END + LINE_BREAK +
                    "IPhone" + TAB + "300" + HTML_BREAK + LINE_BREAK +
                    HTML_END + LINE_BREAK
            )
        );
    }

    @Test
    void shouldReturnSum() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(eq("command"))).thenReturn("sum");

        when(response.getWriter()).thenReturn(writer);

        new QueryServlet().doGet(request, response);

        writer.flush();
        assertThat(
            stringWriter.toString(),
            equalTo(HTML_HEADER + LINE_BREAK +
                    "Summary price: " + LINE_BREAK +
                    "650" + LINE_BREAK +
                    HTML_END + LINE_BREAK
            )
        );
    }

    @Test
    void shouldReturnCount() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(eq("command"))).thenReturn("count");

        when(response.getWriter()).thenReturn(writer);

        new QueryServlet().doGet(request, response);

        writer.flush();
        assertThat(
            stringWriter.toString(),
            equalTo(HTML_HEADER + LINE_BREAK +
                    "Number of products: " + LINE_BREAK +
                    "2" + LINE_BREAK +
                    HTML_END + LINE_BREAK
            )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"someCommand"})
    @NullSource
    void shouldPrintUnknown(String command) throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(eq("command"))).thenReturn(command);

        when(response.getWriter()).thenReturn(writer);

        new QueryServlet().doGet(request, response);

        writer.flush();
        assertThat(
            stringWriter.toString(),
            equalTo("Unknown command: " + command + LINE_BREAK)
        );
    }
}
