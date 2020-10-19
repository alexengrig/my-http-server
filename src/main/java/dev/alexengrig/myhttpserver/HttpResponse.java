package dev.alexengrig.myhttpserver;

import java.util.Objects;

public class HttpResponse {
    private static final String SPACE = " ";
    private static final String NEW_LINE = "\r\n";

    private final HttpVersion version;
    private final HttpStatus status;
    private final HttpHeaders headers;
    private final HttpBody body;


    public HttpResponse(HttpVersion version, HttpStatus status, HttpHeaders headers, HttpBody body) {
        this.version = Objects.requireNonNull(version, "Response version must not be null");
        this.status = Objects.requireNonNull(status, "Response status must not be null");
        this.headers = Objects.requireNonNull(headers, "Response headers must not be null");
        this.body = Objects.requireNonNull(body, "Response body must not be null");
    }

    @Override
    public String toString() {
        return version + SPACE + status + NEW_LINE +
                headers + NEW_LINE + NEW_LINE +
                body;
    }
}
