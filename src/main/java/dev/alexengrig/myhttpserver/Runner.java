package dev.alexengrig.myhttpserver;

public class Runner {
    public static void main(String[] args) throws Exception {
        Server server = new Server("127.0.0.1", 8008);
        server.get("/", (req, res) -> {
            HttpBody body = new HttpBody("<html><body><h1>Your request:</h1><pre>" + req + "</pre></body></html>");
            res.setBody(body);
        });
        server.bootstrap();
    }
}
