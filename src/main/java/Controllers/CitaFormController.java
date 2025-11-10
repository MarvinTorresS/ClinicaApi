package controllers;

import Service.CitaService;
import Service.PacienteService;
import Service.ClinicaService;
import db.Cita;
import db.Paciente;
import db.Clinica;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Date;

public class CitaFormController {

    // ---- UI (ids del FXML) ----
    @FXML private ComboBox<Paciente> cbPaciente;
    @FXML private ComboBox<Clinica> cbClinica;
    @FXML private DatePicker dateCita;
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraFin;
    @FXML private TextField txtDescripcion;
    @FXML private ComboBox<String> cbEstado;
    @FXML private TextField txtIdCita;

    // Filtros
    @FXML private TextField txtFiltroPaciente;
    @FXML private ComboBox<Clinica> cbFiltroClinica;
    @FXML private DatePicker dpFiltroFecha;

    // Tabla
    @FXML private TableView<Cita> tblCitas;
    @FXML private TableColumn<Cita, Long>   tcId;
    @FXML private TableColumn<Cita, String> tcPaciente;
    @FXML private TableColumn<Cita, String> tcClinica;
    @FXML private TableColumn<Cita, String> tcFecha;
    @FXML private TableColumn<Cita, String> tcInicio;
    @FXML private TableColumn<Cita, String> tcFin;
    @FXML private TableColumn<Cita, String> tcEstado;
    @FXML private TableColumn<Cita, String> tcDescripcion;

    @FXML private Label lblEstado;

    // ---- Services (REST) ----
    private final CitaService citaService = new CitaService();
    private final PacienteService pacienteService = new PacienteService();
    private final ClinicaService clinicaService = new ClinicaService();

    // ---- Estado local ----
    private final ObservableList<Cita> citasObs = FXCollections.observableArrayList();
    private List<Cita> cacheCitas = new ArrayList<>();

    // ==================== INIT ====================

    @FXML
    public void initialize() {
        // Estado
        cbEstado.setItems(FXCollections.observableArrayList(
                "PENDIENTE", "CONFIRMADA", "CANCELADA", "ATENDIDA"
        ));

        // Combos
        configurarComboPaciente(cbPaciente);
        configurarComboClinica(cbClinica);
        configurarComboClinica(cbFiltroClinica);

        cargarPacientes();
        cargarClinicas();

        // Tabla
        configurarTabla();

        // Política de resize compatible con JavaFX 13
        tblCitas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblCitas.setPlaceholder(new Label("Sin citas para mostrar"));

        // Datos
        refrescarTabla();
        limpiarFormulario();
    }

    private void configurarComboPaciente(ComboBox<Paciente> combo) {
        combo.setConverter(new StringConverter<>() {
            @Override public String toString(Paciente p) { return p == null ? "" : nombrePaciente(p); }
            @Override public Paciente fromString(String s) { return null; }
        });
        combo.setCellFactory(list -> new ListCell<>() {
            @Override protected void updateItem(Paciente p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? "" : nombrePaciente(p));
            }
        });
    }

    private void configurarComboClinica(ComboBox<Clinica> combo) {
        combo.setConverter(new StringConverter<>() {
            @Override public String toString(Clinica c) { return c == null ? "" : safe(c.getNombre()); }
            @Override public Clinica fromString(String s) { return null; }
        });
        combo.setCellFactory(list -> new ListCell<>() {
            @Override protected void updateItem(Clinica c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? "" : safe(c.getNombre()));
            }
        });
    }

    private void configurarTabla() {
    // --- Id ---
    tcId.setText("Id");
    tcId.setCellValueFactory(c ->
            new javafx.beans.property.ReadOnlyObjectWrapper<>(c.getValue().getIdCita()));
    tcId.setMinWidth(70);

    // --- Paciente ---
    tcPaciente.setText("Paciente");
    tcPaciente.setCellValueFactory(c ->
            new ReadOnlyStringWrapper(
                    c.getValue().getIdPaciente() != null
                            ? nombrePaciente(c.getValue().getIdPaciente())
                            : ""
            ));
    tcPaciente.setMinWidth(150);

    // --- Clínica ---
    tcClinica.setText("Clínica");
    tcClinica.setCellValueFactory(c ->
            new ReadOnlyStringWrapper(
                    c.getValue().getIdClinica() != null
                            ? safe(c.getValue().getIdClinica().getNombre())
                            : ""
            ));
    tcClinica.setMinWidth(150);

    // --- Fecha (dd/MM/yyyy) ---
    tcFecha.setText("Fecha");
    tcFecha.setCellValueFactory(c -> {
        Date d = c.getValue().getFechaCita();
        if (d == null) return new ReadOnlyStringWrapper("");
        LocalDate ld = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String formatted = ld.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return new ReadOnlyStringWrapper(formatted);
    });
    tcFecha.setMinWidth(110);

    // --- Inicio / Fin ---
    tcInicio.setText("Inicio");
    tcInicio.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getHoraInicio())));
    tcInicio.setMinWidth(70);

    tcFin.setText("Fin");
    tcFin.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getHoraFin())));
    tcFin.setMinWidth(70);

    // --- Estado ---
    tcEstado.setText("Estado");
    tcEstado.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getEstado())));
    tcEstado.setMinWidth(110);

    // --- Descripción ---
    tcDescripcion.setText("Descripción");
    tcDescripcion.setCellValueFactory(c -> new ReadOnlyStringWrapper(safe(c.getValue().getDescripcion())));
    tcDescripcion.setMinWidth(200);

    // Política de tamaño: que no se escondan columnas
    tblCitas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    // Evitar reordenar columnas importantes
    for (TableColumn<?, ?> col : tblCitas.getColumns()) {
        col.setReorderable(false);
    }

    tblCitas.setPlaceholder(new Label("Sin citas para mostrar"));
    tblCitas.setItems(citasObs);

    // Cargar selección al formulario
    tblCitas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
        if (sel != null) cargarEnFormulario(sel);
    });
}


    // ==================== LOADERS ====================

    private void cargarPacientes() {
        try {
            List<Paciente> pacientes = pacienteService.listarPacientes();
            cbPaciente.getItems().setAll(pacientes);
        } catch (Exception e) {
            setStatus("❌ Error cargando pacientes: " + e.getMessage());
        }
    }

    private void cargarClinicas() {
        try {
            List<Clinica> clinicas = clinicaService.listarClinicas();
            cbClinica.getItems().setAll(clinicas);
            cbFiltroClinica.getItems().setAll(clinicas);
        } catch (Exception e) {
            setStatus("❌ Error cargando clínicas: " + e.getMessage());
        }
    }

    private void refrescarTabla() {
        try {
            cacheCitas = citaService.listarCitas();

            // Hidrata referencias para que la tabla tenga nombres
            hidratarReferencias(cacheCitas);

            citasObs.setAll(cacheCitas);
            setStatus("✅ Citas cargadas: " + citasObs.size());
        } catch (Exception e) {
            setStatus("❌ Error cargando citas: " + e.getMessage());
        }
    }

    /**
     * Si el backend devuelve paciente/clinica solo con id (o sin nombre),
     * completa los datos consultando los endpoints de detalle UNA sola vez por id.
     */
    private void hidratarReferencias(List<Cita> citas) {
        Map<Long, Paciente> cachePac = new HashMap<>();
        Map<Long, Clinica>  cacheCli = new HashMap<>();

        for (Cita c : citas) {
            // Paciente
            if (c.getIdPaciente() != null) {
                Long idP = c.getIdPaciente().getIdPaciente();
                boolean faltanDatos = isBlank(c.getIdPaciente().getNombre()) && isBlank(c.getIdPaciente().getApellido());
                if (idP != null && faltanDatos) {
                    Paciente full = cachePac.computeIfAbsent(idP, k -> {
                        try { return pacienteService.buscarPorId(k); } catch (Exception ex) { return null; }
                    });
                    if (full != null) c.setIdPaciente(full);
                }
            }
            // Clínica
            if (c.getIdClinica() != null) {
                Long idC = c.getIdClinica().getIdClinica();
                boolean faltanDatos = isBlank(c.getIdClinica().getNombre());
                if (idC != null && faltanDatos) {
                    Clinica full = cacheCli.computeIfAbsent(idC, k -> {
                        try { return clinicaService.buscarPorId(k); } catch (Exception ex) { return null; }
                    });
                    if (full != null) c.setIdClinica(full);
                }
            }
        }
    }

    // ==================== HANDLERS CRUD ====================

    @FXML
    private void onNuevo() {
        tblCitas.getSelectionModel().clearSelection();
        limpiarFormulario();
        setStatus("➕ Nuevo registro listo para llenar.");
    }

    @FXML
    private void onGuardar() {
        if (!validarFormulario(true)) return;

        try {
            Cita c = buildFromForm(null);
            // algunos backends piden id en POST:
            if (c.getIdCita() == null) c.setIdCita(System.currentTimeMillis());

            Cita creada = citaService.registrarCita(c);
            refrescarTabla();
            setStatus("✅ Cita creada (id " + creada.getIdCita() + ")");
            limpiarFormulario();
        } catch (Exception e) {
            setStatus("❌ Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onActualizar() {
        Cita sel = tblCitas.getSelectionModel().getSelectedItem();
        Long id = leerIdFormulario();

        if (sel == null && id == null) {
            setStatus("⚠️ Seleccione una fila o ingrese un Id para actualizar.");
            return;
        }
        if (!validarFormulario(false)) return;

        try {
            Cita c = buildFromForm(id != null ? id : sel.getIdCita());
            Cita upd = citaService.actualizarCita(c);
            refrescarTabla();
            seleccionarEnTabla(upd.getIdCita());
            setStatus("✅ Cita actualizada (id " + upd.getIdCita() + ")");
        } catch (Exception e) {
            setStatus("❌ Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onEliminar() {
        Cita sel = tblCitas.getSelectionModel().getSelectedItem();
        Long id = sel != null ? sel.getIdCita() : leerIdFormulario();

        if (id == null) {
            setStatus("⚠️ Seleccione una fila o ingrese un Id para eliminar.");
            return;
        }

        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar la cita con id " + id + "?", ButtonType.OK, ButtonType.CANCEL);
        a.setHeaderText("Confirmar eliminación");
        a.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    citaService.eliminarCita(id);
                    refrescarTabla();
                    limpiarFormulario();
                    setStatus("🗑️ Cita eliminada (id " + id + ")");
                } catch (Exception e) {
                    setStatus("❌ Error al eliminar: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void onLimpiar() {
        tblCitas.getSelectionModel().clearSelection();
        limpiarFormulario();
        setStatus("🧹 Formulario limpio.");
    }

    // ==================== DISPONIBILIDAD ====================

    @FXML
    private void onVerificarDisponibilidad() {
        if (!validarCamposBasicos()) return;

        try {
            LocalTime ini = parseHora(txtHoraInicio.getText());
            LocalTime fin = parseHora(txtHoraFin.getText());

            boolean libreSrv = citaService.estaDisponible(
                    cbClinica.getValue().getIdClinica(),
                    toDate(dateCita.getValue()),
                    ini.format(DateTimeFormatter.ofPattern("HH:mm")),
                    fin.format(DateTimeFormatter.ofPattern("HH:mm")));

            boolean choqueLocal = hayChoqueLocal(cbClinica.getValue().getIdClinica(),
                    toDate(dateCita.getValue()), ini, fin);

            if (libreSrv && !choqueLocal) setStatus("✅ Horario disponible (server + local).");
            else if (!libreSrv && !choqueLocal) setStatus("ℹ️ Server dice NO, pero localmente no hay choques.");
            else setStatus("⛔ Horario NO disponible (choque local).");
        } catch (Exception e) {
            setStatus("❌ Error verificando disponibilidad: " + e.getMessage());
        }
    }

    // ==================== FILTROS / REFRESH ====================

    @FXML
    private void onAplicarFiltros() {
        if (cacheCitas == null) cacheCitas = new ArrayList<>();
        String filtroNombre = safe(txtFiltroPaciente.getText()).toLowerCase(Locale.ROOT);
        Clinica filtroClinica = cbFiltroClinica.getValue();
        LocalDate filtroFecha = dpFiltroFecha.getValue();

        List<Cita> filtradas = cacheCitas.stream().filter(c -> {
            boolean ok = true;

            if (!filtroNombre.isBlank()) {
                String nom = c.getIdPaciente() != null ? nombrePaciente(c.getIdPaciente()).toLowerCase(Locale.ROOT) : "";
                ok &= nom.contains(filtroNombre);
            }
            if (filtroClinica != null) {
                ok &= (c.getIdClinica() != null && Objects.equals(
                        c.getIdClinica().getIdClinica(), filtroClinica.getIdClinica()));
            }
            if (filtroFecha != null && c.getFechaCita() != null) {
                LocalDate ld = c.getFechaCita().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                ok &= ld.equals(filtroFecha);
            }
            return ok;
        }).collect(Collectors.toList());

        citasObs.setAll(filtradas);
        setStatus("🔎 Filtrado: " + filtradas.size() + " resultado(s).");
    }

    @FXML
    private void onLimpiarFiltros() {
        txtFiltroPaciente.clear();
        cbFiltroClinica.setValue(null);
        dpFiltroFecha.setValue(null);
        citasObs.setAll(cacheCitas);
        setStatus("✨ Filtros limpiados.");
    }

    @FXML
    private void onRefrescar() {
        refrescarTabla();
    }

    // ==================== MENÚ CONTEXTUAL ====================

    @FXML
    private void onEditarSeleccion() {
        Cita sel = tblCitas.getSelectionModel().getSelectedItem();
        if (sel == null) {
            setStatus("⚠️ Seleccione una fila para editar.");
            return;
        }
        cargarEnFormulario(sel);
        setStatus("✏️ Cita cargada para edición (id " + sel.getIdCita() + ").");
    }

    @FXML
    private void onEliminarSeleccion() {
        Cita sel = tblCitas.getSelectionModel().getSelectedItem();
        if (sel == null) {
            setStatus("⚠️ Seleccione una fila para eliminar.");
            return;
        }
        onEliminar();
    }

    // ==================== HELPERS ====================

    private void cargarEnFormulario(Cita c) {
        txtIdCita.setText(c.getIdCita() != null ? String.valueOf(c.getIdCita()) : "");
        cbPaciente.setValue(c.getIdPaciente());
        cbClinica.setValue(c.getIdClinica());
        if (c.getFechaCita() != null) {
            dateCita.setValue(c.getFechaCita().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            dateCita.setValue(null);
        }
        txtHoraInicio.setText(safe(c.getHoraInicio()));
        txtHoraFin.setText(safe(c.getHoraFin()));
        txtDescripcion.setText(safe(c.getDescripcion()));
        cbEstado.setValue(safe(c.getEstado()).isBlank() ? "PENDIENTE" : c.getEstado());
    }

    private void limpiarFormulario() {
        txtIdCita.clear();
        cbPaciente.setValue(null);
        cbClinica.setValue(null);
        dateCita.setValue(null);
        txtHoraInicio.clear();
        txtHoraFin.clear();
        txtDescripcion.clear();
        cbEstado.setValue("PENDIENTE");
    }

    private Cita buildFromForm(Long id) {
        Paciente p = cbPaciente.getValue();
        Clinica c = cbClinica.getValue();

        Cita cita = new Cita();
        if (id != null) cita.setIdCita(id); // update
        cita.setIdPaciente(p);
        cita.setIdClinica(c);

        // normaliza a HH:mm
        String iniN = normalizarHora(txtHoraInicio.getText());
        String finN = normalizarHora(txtHoraFin.getText());

        cita.setHoraInicio(iniN);
        cita.setHoraFin(finN);

        cita.setFechaCita(toDate(dateCita.getValue()));
        cita.setDescripcion(safe(txtDescripcion.getText()).trim());
        cita.setEstado(cbEstado.getValue() == null ? "PENDIENTE" : cbEstado.getValue());
        return cita;
    }

    private boolean validarFormulario(boolean creando) {
        if (!validarCamposBasicos()) return false;

        LocalTime ini, fin;
        try {
            ini = parseHora(txtHoraInicio.getText());
            fin = parseHora(txtHoraFin.getText());
        } catch (DateTimeParseException e) {
            setStatus("⚠️ Formato de hora inválido. Usa 9:00 o 09:00 (HH:mm).");
            return false;
        }
        if (!ini.isBefore(fin)) {
            setStatus("⚠️ La hora de inicio debe ser menor que la hora de fin.");
            return false;
        }

        // Verificación remota + fallback local
        try {
            boolean libreSrv = citaService.estaDisponible(cbClinica.getValue().getIdClinica(),
                    toDate(dateCita.getValue()),
                    ini.format(DateTimeFormatter.ofPattern("HH:mm")),
                    fin.format(DateTimeFormatter.ofPattern("HH:mm")));

            if (!libreSrv) {
                boolean choqueLocal = hayChoqueLocal(cbClinica.getValue().getIdClinica(),
                        toDate(dateCita.getValue()), ini, fin);
                if (choqueLocal) {
                    setStatus("⛔ Ese rango horario NO está disponible (choque local).");
                    return false;
                } else {
                    // permitir continuar pero avisar
                    setStatus("ℹ️ No se confirmó con el servidor, pero no hay choque local. Se intentará guardar.");
                }
            }
        } catch (Exception e) {
            // si el endpoint falla, permitir guardar si localmente no hay choque
            boolean choqueLocal = hayChoqueLocal(cbClinica.getValue().getIdClinica(),
                    toDate(dateCita.getValue()), ini, fin);
            if (choqueLocal) {
                setStatus("⛔ Choque local con otra cita. Ajusta el horario.");
                return false;
            }
            setStatus("⚠️ No se pudo verificar disponibilidad en el servidor. Se intentará guardar.");
        }

        // Normaliza en los campos para que el usuario vea HH:mm
        txtHoraInicio.setText(ini.format(DateTimeFormatter.ofPattern("HH:mm")));
        txtHoraFin.setText(fin.format(DateTimeFormatter.ofPattern("HH:mm")));
        return true;
    }

    private boolean validarCamposBasicos() {
        if (cbPaciente.getValue() == null || cbClinica.getValue() == null || dateCita.getValue() == null) {
            setStatus("⚠️ Seleccione paciente, clínica y fecha.");
            return false;
        }
        if (isBlank(txtHoraInicio.getText()) || isBlank(txtHoraFin.getText())) {
            setStatus("⚠️ Ingrese hora de inicio y hora fin.");
            return false;
        }
        return true;
    }

    private Long leerIdFormulario() {
        try {
            String t = txtIdCita.getText();
            return (t == null || t.isBlank()) ? null : Long.parseLong(t.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void seleccionarEnTabla(Long id) {
        if (id == null) return;
        for (int i = 0; i < citasObs.size(); i++) {
            if (Objects.equals(citasObs.get(i).getIdCita(), id)) {
                tblCitas.getSelectionModel().select(i);
                tblCitas.scrollTo(i);
                break;
            }
        }
    }

    private String nombrePaciente(Paciente p) {
        String n = safe(p.getNombre());
        String a = safe(p.getApellido());
        String full = (n + " " + a).trim();
        return full.isBlank() ? safe(n) : full;
    }

    private String formatearFecha(Date d) {
        if (d == null) return "";
        LocalDate ld = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ld.toString();
    }

    private Date toDate(LocalDate ld) {
        if (ld == null) return null;
        return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private void setStatus(String s) { lblEstado.setText(s); }
    private boolean isBlank(String s) { return s == null || s.trim().isBlank(); }
    private String safe(String s) { return s == null ? "" : s; }

    // ==================== HORAS / DISPONIBILIDAD LOCAL ====================

    // Acepta "9:00" y "09:00" -> devuelve LocalTime
    private LocalTime parseHora(String s) {
        if (s == null) throw new DateTimeParseException("null", "", 0);
        s = s.trim();
        try { return LocalTime.parse(s, DateTimeFormatter.ofPattern("H:mm")); }
        catch (DateTimeParseException ignore) { }
        return LocalTime.parse(s, DateTimeFormatter.ofPattern("HH:mm"));
    }

    // Normaliza cualquier entrada a "HH:mm"
    private String normalizarHora(String s) {
        return parseHora(s).format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private boolean haySolape(LocalTime aIni, LocalTime aFin, LocalTime bIni, LocalTime bFin) {
        // [aIni, aFin) solapa con [bIni, bFin)?
        return aIni.isBefore(bFin) && bIni.isBefore(aFin);
    }

    // Fallback local por si el endpoint de disponibilidad falla o responde "false" erróneo
    private boolean hayChoqueLocal(Long idClinica, Date fecha, LocalTime ini, LocalTime fin) {
        if (cacheCitas == null) return false;
        LocalDate d = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        for (Cita c : cacheCitas) {
            if (c.getIdClinica() == null || c.getFechaCita() == null) continue;
            if (!Objects.equals(c.getIdClinica().getIdClinica(), idClinica)) continue;

            LocalDate dc = c.getFechaCita().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (!d.equals(dc)) continue;

            try {
                LocalTime ci = parseHora(safe(c.getHoraInicio()));
                LocalTime cf = parseHora(safe(c.getHoraFin()));
                if (haySolape(ini, fin, ci, cf)) return true;
            } catch (Exception ignore) {
                // si hay horas mal formateadas en datos viejos, las ignoro
            }
        }
        return false;
    }
}
