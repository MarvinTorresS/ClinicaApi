/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package controllers;

import Service.PacienteService;
import db.Paciente;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.Date;

public class PacienteFormController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private DatePicker dateNacimiento;
    @FXML private Label lblEstado;

    private final PacienteService pacienteService = new PacienteService();

    @FXML
    private void registrarPaciente() {
        try {
            Paciente p = new Paciente();
            p.setIdPaciente(System.currentTimeMillis()); // ID temporal
            p.setNombre(txtNombre.getText());
            p.setApellido(txtApellido.getText());
            p.setTelefono(txtTelefono.getText());
            p.setCorreo(txtCorreo.getText());
            p.setFechaNac(java.sql.Date.valueOf(dateNacimiento.getValue()));
            p.setFechaRegistro(new Date());

            pacienteService.registrarPaciente(p);
            lblEstado.setText("✅ Paciente registrado correctamente");
        } catch (Exception e) {
            lblEstado.setText("❌ Error al registrar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
