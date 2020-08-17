package dev.alexengrig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        String host = args[0];
        try (Socket socket = new Socket(host, 80)) {
            byte[] data = ("GET /solrsearch/select?q=guice&rows=22&wt=json HTTP/1.1\n" +
                    "Host: " + host + "\n\n").getBytes();
            OutputStream request = socket.getOutputStream();
            request.write(data);
            InputStream response = socket.getInputStream();
            System.out.println("-BEGIN-");
            for (int ch = 0; ch != -1; ch = response.read()) {
                System.out.print((char) ch);
            }
            response.close();
            System.out.println("-END-");
        }
    }
}
