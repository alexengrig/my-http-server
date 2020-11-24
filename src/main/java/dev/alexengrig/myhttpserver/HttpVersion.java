package dev.alexengrig.myhttpserver;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1");

    private String value;

    HttpVersion(String value) {
        this.value = value;
    }

    public static HttpVersion of(String version) {
        for (HttpVersion httpVersion : values()) {
            if (httpVersion.value.equals(version)) {
                return httpVersion;
            }
        }
        throw new IllegalArgumentException("Unknown HTTP version: " + version);
    }

    @Override
    public String toString() {
        return value;
    }
}
