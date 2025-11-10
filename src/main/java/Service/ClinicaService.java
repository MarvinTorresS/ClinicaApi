package Service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import db.Clinica;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class ClinicaService {
    private static final String BASE = "/api/clinicas";

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, t, c) ->
                    src == null ? JsonNull.INSTANCE : new JsonPrimitive(src.getTime()))
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, t, c) -> {
                if (json == null || json.isJsonNull()) return null;
                var p = json.getAsJsonPrimitive();
                if (p.isNumber()) return new Date(p.getAsLong());
                try { return new Date(json.getAsString()); } catch (Exception e) { return null; }
            })
            .serializeNulls()
            .create();

    // Alias por compatibilidad
    public Clinica registrarClinica(Clinica c) { return crearClinica(c); }

    public Clinica crearClinica(Clinica c) {
        try {
            String res = ApiClient.post(BASE, gson.toJson(c));
            return gson.fromJson(res, Clinica.class);
        } catch (Exception e) {
            throw new RuntimeException("Error creando clínica", e);
        }
    }

    public List<Clinica> listarClinicas() {
        try {
            String res = ApiClient.get(BASE);
            Type lt = new TypeToken<List<Clinica>>(){}.getType();
            return gson.fromJson(res, lt);
        } catch (Exception e) {
            throw new RuntimeException("Error listando clínicas", e);
        }
    }

    public Clinica buscarPorId(Long id) {
        try {
            String res = ApiClient.get(BASE + "/" + id);
            return gson.fromJson(res, Clinica.class);
        } catch (Exception e) {
            throw new RuntimeException("Error buscando clínica id=" + id, e);
        }
    }

    public Clinica actualizarClinica(Clinica c) {
        if (c.getIdClinica() == null) throw new IllegalArgumentException("Id requerido");
        try {
            String res = ApiClient.put(BASE + "/" + c.getIdClinica(), gson.toJson(c));
            return gson.fromJson(res, Clinica.class);
        } catch (Exception e) {
            throw new RuntimeException("Error actualizando clínica id=" + c.getIdClinica(), e);
        }
    }

    public void eliminarClinica(Long id) {
        try { ApiClient.delete(BASE + "/" + id); }
        catch (Exception e) { throw new RuntimeException("Error eliminando clínica id=" + id, e); }
    }
}
