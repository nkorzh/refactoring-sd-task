package ru.akirakozov.sd.refactoring.product.web;

import ru.akirakozov.sd.refactoring.exception.InternalServerError;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static ru.akirakozov.sd.refactoring.product.web.HtmlUtils.HEADER_END;
import static ru.akirakozov.sd.refactoring.product.web.HtmlUtils.HEADER_START;
import static ru.akirakozov.sd.refactoring.product.web.HtmlUtils.HTML_CONTENT_TYPE;
import static ru.akirakozov.sd.refactoring.product.web.HtmlUtils.HTML_END;
import static ru.akirakozov.sd.refactoring.product.web.HtmlUtils.HTML_HEADER;

public final class ResponseBuilder {
    private String header;
    private String body;
    private String errorMsg;
    private boolean useHeader = true;

    private final HttpServletResponse response;

    public static ResponseBuilder of(HttpServletResponse response) {
        return new ResponseBuilder(response);
    }

    private ResponseBuilder(HttpServletResponse response) {
        this.response = response;
    }

    public ResponseBuilder withHeader(String header) {
        this.header = header;
        return this;
    }

    public ResponseBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public ResponseBuilder withError(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public ResponseBuilder setHeaderUsage(boolean useHeader) {
        this.useHeader = useHeader;
        return this;
    }

    public void build() {
        response.setContentType(HTML_CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            PrintWriter writer = response.getWriter();
            if (errorMsg != null) {
                writer.println(errorMsg);
                return;
            }
            if (useHeader) {
                writer.println(HTML_HEADER);
            }
            if (header != null) {
                writer.print(HEADER_START);
                writer.print(header);
                writer.println(HEADER_END);
            }
            writer.println(body);
            if (useHeader) {
                writer.println(HTML_END);
            }
        } catch (IOException e) {
            throw new InternalServerError("Could not write response: " + e.getMessage());
        }
    }
}
