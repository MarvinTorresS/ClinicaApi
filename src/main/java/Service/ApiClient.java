package Service;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080"; // ajusta si cambias puerto
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public static String get(String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET()
                .build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res.body();
    }

    public static String post(String path, String json) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res.body();
    }

    public static String put(String path, String json) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res.body();
    }

    public static void delete(String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .DELETE()
                .build();
        client.send(req, HttpResponse.BodyHandlers.discarding());
    }
}
