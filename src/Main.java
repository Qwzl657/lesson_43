import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) {
        try {
            HttpServer server = makeServer();
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
}