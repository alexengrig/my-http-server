package dev.alexengrig.myhttpserver;

public class HttpResponse {
    private static final String SPACE = " ";
    private static final String NEW_LINE = "\r\n";

    private HttpVersion version;
    private HttpStatus status;
    private HttpHeaders headers;
    private HttpBody body;

    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public void setBody(HttpBody body) {
        this.body = body;
    }

    public byte[] getBytes() {
        return toString().getBytes();
    }

    @Override
    public String toString() {
        return version + SPACE + status + NEW_LINE +
                headers + NEW_LINE + NEW_LINE +
                body;
    }
}
