/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package controllers;

import Service.ClinicaService;
import db.Clinica;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ClinicaFormController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private Label lblEstado;

    private final ClinicaService clinicaService = new ClinicaService();

    @FXML
    private void registrarClinica() {
        try {
            Clinica c = new Clinica();
            c.setIdClinica(System.currentTimeMillis()); // ID temporal
            c.setNombre(txtNombre.getText());
            c.setDireccion(txtDireccion.getText());
            c.setTelefono(txtTelefono.getText());
            c.setCorreo(txtCorreo.getText());

            clinicaService.registrarClinica(c);
            lblEstado.setText("✅ Clínica registrada correctamente");
            limpiarCampos();
        } catch (Exception e) {
            lblEstado.setText("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtCorreo.clear();
    }
}

