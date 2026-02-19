import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        try {
            HttpServer server = makeServer();
            initRoutes(server);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static HttpServer makeServer() throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost", 8089);
        System.out.println("Server started: http://localhost:8089");
        return HttpServer.create(address, 50);
    }

    private static void initRoutes(HttpServer server) {
        server.createContext("/", Main::handleRoot);
        server.createContext("/apps", Main::handleApps);
        server.createContext("/apps/profile", Main::handleProfile);
        server.createContext("/static", Main::handleFile);
    }



    private static void handleRoot(HttpExchange exchange) throws IOException {
        sendText(exchange, "ROOT handler работает");
    }

    private static void handleApps(HttpExchange exchange) throws IOException {
        sendText(exchange, "APPS handler работает");
    }

    private static void handleProfile(HttpExchange exchange) throws IOException {
        sendText(exchange, "PROFILE handler работает");
    }

    private static void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders()
                .add("Content-Type", "text/plain; charset=utf-8");

        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }



    private static void handleFile(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String filePath = uri.getPath().replace("/static", "");

        if (filePath.isEmpty() || filePath.equals("/")) {
            filePath = "/index.html";
        }

        Path path = Path.of("homework", filePath);

        if (!Files.exists(path)) {
            send404(exchange);
            return;
        }

        byte[] data = Files.readAllBytes(path);
        exchange.sendResponseHeaders(200, data.length);
        exchange.getResponseBody().write(data);
        exchange.close();
    }

    private static void send404(HttpExchange exchange) throws IOException {
        String msg = "404 Not Found";
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(404, data.length);
        exchange.getResponseBody().write(data);
        exchange.close();
    }
}