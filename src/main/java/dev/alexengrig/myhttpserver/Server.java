package dev.alexengrig.myhttpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private final int bufferSize;
    private final HttpRequestFactory requestFactory;
    private final Map<HttpMethod, Map<String, HttpHandler>> handlers;

    private final String host;
    private final int port;

    public Server(String host, int port) {
        this.bufferSize = 256;
        this.requestFactory = new HttpRequestFactory();
        this.handlers = new HashMap<>(HttpMethod.values().length);
        this.host = host;
        this.port = port;
    }

    public void get(String url, HttpHandler handler) {
        Map<String, HttpHandler> handlerByUrl;
        if (handlers.containsKey(HttpMethod.GET)) {
            handlerByUrl = handlers.get(HttpMethod.GET);
        } else {
            handlerByUrl = new HashMap<>();
            handlers.put(HttpMethod.GET, handlerByUrl);
        }
        handlerByUrl.put(url, handler);
    }

    public void bootstrap() throws Exception {
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
            HttpMethod method = request.getMethod();
            String url = request.getUrl();
            System.out.println(method + " " + url);
            HttpResponse response = getResponse();
            if (handlers.containsKey(method)) {
                Map<String, HttpHandler> handlerByUrl = handlers.get(method);
                HttpHandler handler;
                if (handlerByUrl.containsKey(url) && (handler = handlerByUrl.get(url)) != null) {
                    try {
                        handler.handle(request, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                        response.setBody(new HttpBody("<html><body><h1>Error happens</h1></body></html>"));
                    }
                } else {
                    response.setStatus(HttpStatus.NOT_FOUND);
                    response.setBody(new HttpBody("<html><body><h1>Resource not found</h1><pre>" + url +
                            "</pre></body></html>"));
                }
            } else {
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setBody(new HttpBody("<html><body><h1>Method not supported</h1></body></html>"));
            }
            client.write(ByteBuffer.wrap(response.getBytes()));
            client.close();
            System.out.println("Client is closed");
        }
    }

    private AsynchronousServerSocketChannel createServerChannel(String hostname, int port) throws IOException {
        return AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(hostname, port));
    }

    private HttpRequest getRequest(AsynchronousSocketChannel readChannel) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
        StringBuilder stringBuilder = new StringBuilder();
        int readCount;
        boolean reading;
        do {
            readCount = readChannel.read(byteBuffer).get();
            reading = readCount == bufferSize;
            byteBuffer.flip();
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
            stringBuilder.append(charBuffer);
            byteBuffer.clear();
        } while (reading);
        return requestFactory.createRequest(stringBuilder.toString());
    }

    private HttpResponse getResponse() {
        HttpResponse response = new HttpResponse();
        response.setVersion(HttpVersion.HTTP_1_1);
        response.setHeaders(new HttpHeaders(List.of(new HttpHeader("Content-Type", "text/html; charset=UTF-8"))));
        return response;
    }
}
