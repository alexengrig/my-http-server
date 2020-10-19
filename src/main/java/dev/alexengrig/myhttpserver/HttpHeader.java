package dev.alexengrig.myhttpserver;

import java.util.Objects;

public class HttpHeader {
    private static final String DELIMITER = ": ";

    private final String name;
    private final String value;

    public HttpHeader(String name, String value) {
        this.name = Objects.requireNonNull(name, "Header name must not be null");
        this.value = Objects.requireNonNull(value, "Header value must not be null");
    }

    public static HttpHeader parse(String line) {
        String[] parts = Objects.requireNonNull(line, "Header line must not be null").split(DELIMITER);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Header line is invalid: " + line);
        }
        return new HttpHeader(parts[0].trim(), parts[1].trim());
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return name + DELIMITER + (value);
    }
}
