package dev.alexengrig.myhttpserver;

public interface HttpHandler {
    void handle(HttpRequest request, HttpResponse response);
}
