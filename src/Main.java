import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
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
        String host = "localhost";
        InetSocketAddress address = new InetSocketAddress(host, 8089);

        System.out.printf("Сервер запущен: http://%s:%s%n",
                address.getHostName(), address.getPort());

        return HttpServer.create(address, 50);
    }

    private static void initRoutes(HttpServer server) {
        server.createContext("/", Main::handleRoot);
        server.createContext("/apps", Main::handleApps);
        server.createContext("/apps/profile", Main::handleProfile);
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
}