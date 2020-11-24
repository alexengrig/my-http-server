package dev.alexengrig.myhttpserver;

import java.util.ArrayList;

public class HttpRequestFactory {
    private static final String BODY_DELIMITER = "\r\n\r\n";
    private static final String NEW_LINE = "\r\n";
    private static final String START_LINE_DELIMITER = " ";
    private static final String HEADER_DELIMITER = ":";

    public HttpRequest createRequest(String message) {
        String[] parts = message.split(BODY_DELIMITER);
        String top = parts[0];
        String[] topParts = top.split(NEW_LINE);
        String startLine = topParts[0];
        String[] startLineParts = startLine.split(START_LINE_DELIMITER);
        HttpMethod method = HttpMethod.of(startLineParts[0]);
        String url = startLineParts[1];
        HttpVersion version = HttpVersion.of(startLineParts[2]);
        ArrayList<HttpHeader> headerList = new ArrayList<>(topParts.length - 1);
        for (int i = 1; i < topParts.length; i++) {
            String[] headerParts = topParts[i].split(HEADER_DELIMITER, 2);
            String headerName = headerParts[0];
            String headerValue = headerParts[1];
            headerList.add(new HttpHeader(headerName, headerValue));
        }
        HttpHeaders headers = new HttpHeaders(headerList);
        HttpBody body = parts.length == 2 ? new HttpBody(parts[1]) : null;
        return new HttpRequest(message, method, url, version, headers, body);
    }
}
