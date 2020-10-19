package dev.alexengrig.myhttpserver;

import java.util.Objects;

public class HttpBody {
    private final String value;

    public HttpBody(String value) {
        this.value = Objects.requireNonNull(value, "Body value must not be null");
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
