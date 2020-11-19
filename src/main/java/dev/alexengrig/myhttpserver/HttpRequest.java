package dev.alexengrig.myhttpserver;

public class HttpRequest {
    private final String message;

    public HttpRequest(CharSequence message) {
        this.message = message.toString();
    }

    @Override
    public String toString() {
        return message;
    }
}
