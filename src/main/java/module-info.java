module clinica.projecto.clinica {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires java.sql;
    requires org.eclipse.persistence.core;
    requires org.eclipse.persistence.jpa;
    requires java.base;
    requires java.instrument;
    requires java.net.http;
    requires com.google.gson;

    // Abre modelos a EclipseLink (si aún lo usas), a Gson y a JavaFX (PropertyValueFactory)
    opens db to org.eclipse.persistence.core, org.eclipse.persistence.jpa, com.google.gson, javafx.base;

    // FXML carga controllers por reflexión
    opens controllers to javafx.fxml;
    opens clinica.projecto.clinica to javafx.fxml;

    // Exports (lo dejo como lo tenías)
    exports clinica.projecto.clinica;
    exports Service;
    exports db;
}
