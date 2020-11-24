package dev.alexengrig.myhttpserver;

public class HttpRequest {
    private final String message;
    private final HttpMethod method;
    private final String url;
    private final HttpVersion version;
    private final HttpHeaders headers;
    private final HttpBody body;

    public HttpRequest(CharSequence message, HttpMethod method,
                       String url, HttpVersion version,
                       HttpHeaders headers, HttpBody body) {
        this.message = message.toString();
        this.method = method;
        this.url = url;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return message;
    }
}
