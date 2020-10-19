package dev.alexengrig.myhttpserver;

import java.util.Arrays;

public enum HttpStatus {
    OK(200, "OK");

    private final int code;
    private final String definition;

    HttpStatus(int code, String definition) {
        this.code = code;
        this.definition = definition;
    }

    public static HttpStatus valueOf(int code) {
        return Arrays.stream(values())
                .filter(s -> s.code == code)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No HTTP status with code: " + code));
    }

    public int code() {
        return code;
    }

    public String getDefinition() {
        return definition;
    }

    @Override
    public String toString() {
        return code + " " + definition;
    }
}
