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

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        AsynchronousServerSocketChannel server = createServerChannel("127.0.0.1", 8008);
        System.out.println("Started");
        while (server.isOpen()) {
            AsynchronousSocketChannel clientChannel = server.accept().get();
            System.out.println("Has a client");
            if (clientChannel == null || !clientChannel.isOpen()) continue;
            ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            StringBuilder stringBuilder = new StringBuilder();
            boolean reading;
            int readCount;
            do {
                readCount = clientChannel.read(byteBuffer).get();
                reading = readCount == BUFFER_SIZE;
                byteBuffer.flip();
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
                stringBuilder.append(charBuffer);
                byteBuffer.clear();
            } while (reading);
            System.out.println("Client content:\n" + stringBuilder);
            HttpResponse res = new HttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpStatus.OK,
                    new HttpHeaders(List.of(new HttpHeader("Content-Type", "text/html"))),
                    new HttpBody("<html><body><h1>This is response!</h1></body></html>"));
            clientChannel.write(ByteBuffer.wrap(res.toString().getBytes()));
            clientChannel.close();
            System.out.println("Client is closed");
        }

    }

    private static AsynchronousServerSocketChannel createServerChannel(String hostname, int port) throws IOException {
        return AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(hostname, port));
    }
}
