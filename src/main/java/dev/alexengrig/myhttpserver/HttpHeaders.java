package dev.alexengrig.myhttpserver;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpHeaders {
    private static final String NEW_LINE = "\r\n";

    private final List<HttpHeader> values;

    public HttpHeaders(List<HttpHeader> values) {
        this.values = values;
    }

    public static HttpHeaders parse(String line) {
        String[] parts = Objects.requireNonNull(line, "Headers line must not be null").split(NEW_LINE);
        return new HttpHeaders(Arrays.stream(parts).map(HttpHeader::parse).collect(Collectors.toList()));
    }

    public List<HttpHeader> values() {
        return Collections.unmodifiableList(values);
    }

    @Override
    public String toString() {
        return values.stream().map(HttpHeader::toString).collect(Collectors.joining(NEW_LINE));
    }
}
