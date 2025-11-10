package Service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import db.Paciente;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class PacienteService {
    private static final String BASE = "/api/pacientes";

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
    public Paciente registrarPaciente(Paciente p) { return crearPaciente(p); }

    public Paciente crearPaciente(Paciente p) {
        try {
            String res = ApiClient.post(BASE, gson.toJson(p));
            return gson.fromJson(res, Paciente.class);
        } catch (Exception e) {
            throw new RuntimeException("Error creando paciente", e);
        }
    }

    public List<Paciente> listarPacientes() {
        try {
            String res = ApiClient.get(BASE);
            Type lt = new TypeToken<List<Paciente>>(){}.getType();
            return gson.fromJson(res, lt);
        } catch (Exception e) {
            throw new RuntimeException("Error listando pacientes", e);
        }
    }

    public Paciente buscarPorId(Long id) {
        try {
            String res = ApiClient.get(BASE + "/" + id);
            return gson.fromJson(res, Paciente.class);
        } catch (Exception e) {
            throw new RuntimeException("Error buscando paciente id=" + id, e);
        }
    }

    public Paciente actualizarPaciente(Paciente p) {
        if (p.getIdPaciente() == null) throw new IllegalArgumentException("Id requerido");
        try {
            String res = ApiClient.put(BASE + "/" + p.getIdPaciente(), gson.toJson(p));
            return gson.fromJson(res, Paciente.class);
        } catch (Exception e) {
            throw new RuntimeException("Error actualizando paciente id=" + p.getIdPaciente(), e);
        }
    }

    public void eliminarPaciente(Long id) {
        try { ApiClient.delete(BASE + "/" + id); }
        catch (Exception e) { throw new RuntimeException("Error eliminando paciente id=" + id, e); }
    }
}
