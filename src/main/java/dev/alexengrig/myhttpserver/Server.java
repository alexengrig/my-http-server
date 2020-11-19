package dev.alexengrig.myhttpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Server {
    public static final int BUFFER_SIZE = 256;

    private final String host;
    private final int port;
    private final HttpHandler handler;

    public Server(String host, int port, HttpHandler handler) {
        this.host = host;
        this.port = port;
        this.handler = handler;
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        new Server("127.0.0.1", 8008, (req, res) -> {
            res.setBody(new HttpBody("<html><body><h1>Your request:</h1><pre>" + req + "</pre></body></html>"));
        }).bootstrap();
    }

    private static HttpRequest getRequest(AsynchronousSocketChannel readChannel)
            throws InterruptedException, ExecutionException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        StringBuilder stringBuilder = new StringBuilder();
        int readCount;
        boolean reading;
        do {
            readCount = readChannel.read(byteBuffer).get();
            reading = readCount == BUFFER_SIZE;
            byteBuffer.flip();
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
            stringBuilder.append(charBuffer);
            byteBuffer.clear();
        } while (reading);
        return new HttpRequest(stringBuilder);
    }

    private static AsynchronousServerSocketChannel createServerChannel(String hostname, int port) throws IOException {
        return AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(hostname, port));
    }

    private static HttpResponse getResponse(AsynchronousSocketChannel client) {
        HttpResponse response = new HttpResponse();
        response.setVersion(HttpVersion.HTTP_1_1);
        response.setHeaders(new HttpHeaders(List.of(new HttpHeader("Content-Type", "text/html; charset=UTF-8"))));
        return response;
    }

    public void bootstrap() throws IOException, ExecutionException, InterruptedException {
        System.out.println("Started");
        AsynchronousServerSocketChannel server = createServerChannel(host, port);
        while (server.isOpen()) {
            AsynchronousSocketChannel client = server.accept().get();
            System.out.println("Has a client");
            if (client == null || !client.isOpen()) {
                System.out.println("Lost a client");
                continue;
            }
            HttpRequest request = getRequest(client);
            HttpResponse response = getResponse(client);
            if (handler != null) {
                try {
                    handler.handle(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    response.setBody(new HttpBody("<html><body><h1>Error happens</h1></body></html>"));
                }
            } else {
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setBody(new HttpBody("<html><body><h1>Resource not found</h1></body></html>"));
            }
            client.write(ByteBuffer.wrap(response.getBytes()));
            client.close();
            System.out.println("Client is closed");
        }
    }
}
