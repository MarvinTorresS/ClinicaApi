/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class MainViewController {

    @FXML
    private AnchorPane contentArea;

    @FXML
    public void initialize() {
        // Mostrar el formulario de pacientes por defecto
        showPacienteForm();
    }

    @FXML
    private void showPacienteForm() {
        loadView("/fxml/PacienteForm.fxml");
    }

    @FXML
    private void showClinicaForm() {
        loadView("/fxml/ClinicaForm.fxml");
    }

    @FXML
    private void showCitaForm() {
        loadView("/fxml/CitaForm.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

