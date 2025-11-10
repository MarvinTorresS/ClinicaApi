package Service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import db.Cita;
import db.Clinica;
import db.Paciente;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class CitaService {

    private static final String BASE = "http://localhost:8080/api/citas";

    // 👇 fuerza el formato de fecha que devuelve/recibe tu API ("2025-11-16")
    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    public List<Cita> listarCitas() throws Exception {
        var req = HttpRequest.newBuilder(URI.create(BASE))
                .GET().build();
        var res = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() / 100 != 2) throw new RuntimeException("Error " + res.statusCode());

        var type = new TypeToken<List<Cita>>(){}.getType();
        return GSON.fromJson(res.body(), type);
    }

    public Cita registrarCita(Cita c) throws Exception {
        var body = GSON.toJson(c); // 👈 serializa fecha como yyyy-MM-dd
        var req = HttpRequest.newBuilder(URI.create(BASE))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        var res = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() / 100 != 2) throw new RuntimeException("Error " + res.statusCode());
        return GSON.fromJson(res.body(), Cita.class);
    }

    public Cita actualizarCita(Cita c) throws Exception {
        var body = GSON.toJson(c);
        var req = HttpRequest.newBuilder(URI.create(BASE + "/" + c.getIdCita()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        var res = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() / 100 != 2) throw new RuntimeException("Error " + res.statusCode());
        return GSON.fromJson(res.body(), Cita.class);
    }

    public void eliminarCita(Long id) throws Exception {
        var req = HttpRequest.newBuilder(URI.create(BASE + "/" + id))
                .DELETE().build();
        var res = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() / 100 != 2) throw new RuntimeException("Error " + res.statusCode());
    }

    public boolean estaDisponible(Long idClinica, Date fecha, String ini, String fin) throws Exception {
        // si tu endpoint espera yyyy-MM-dd, enviamos así
        String f = java.time.Instant.ofEpochMilli(fecha.getTime())
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate().toString(); // yyyy-MM-dd

        var uri = String.format("%s/disponible?idClinica=%d&fecha=%s&ini=%s&fin=%s",
                BASE, idClinica, f, ini, fin);
        var req = HttpRequest.newBuilder(URI.create(uri)).GET().build();
        var res = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() / 100 != 2) return true; // si falla, no bloqueamos
        return Boolean.parseBoolean(res.body());
    }
}
