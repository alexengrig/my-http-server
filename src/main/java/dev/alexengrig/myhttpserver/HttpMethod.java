package dev.alexengrig.myhttpserver;

public enum HttpMethod {
    GET,
    POST;

    public static HttpMethod of(String method) {
        for (HttpMethod httpMethod : values()) {
            if (httpMethod.name().equalsIgnoreCase(method)) {
                return httpMethod;
            }
        }
        throw new IllegalArgumentException("Unknown HTTP method: " + method);
    }
}
