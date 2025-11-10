ClínicaApp (JavaFX + Maven) — Cliente de Gestión de Citas

Aplicación JavaFX que consume una API REST para gestionar Pacientes, Clínicas y Citas.
Este módulo es solo el cliente de escritorio: no accede a la base de datos directamente; todas las operaciones (CRUD) se hacen contra el backend REST.

Script de base de datos
En este repositorio se incluye el archivo Script tablas .sql (en la raíz) con las tablas necesarias para el backend.
Ejecútalo en tu RDBMS (MySQL / PostgreSQL / etc.) antes de levantar la API, y configura las credenciales en el proyecto REST.

🧱 Funcionalidades

Citas: CRUD completo (crear, listar, actualizar, eliminar).

Verificación de disponibilidad de horario en la clínica.

Filtros por nombre de paciente, clínica y fecha.

Tabla con: Id, Paciente, Clínica, Fecha (dd/MM/yyyy), Inicio, Fin, Estado, Descripción.

Validaciones de horas (soporta 9:00 o 09:00, normaliza a HH:mm) y inicio < fin.

🏗️ Arquitectura

Cliente: Java 21, JavaFX, Maven, Gson (para JSON).

Backend: API REST (JAX-RS/Spring), JPA (EclipseLink/Hibernate).

Este repo NO incluye el backend. Debes clonar/levantar el proyecto REST aparte y apuntar la URL base desde el cliente.

✅ Requisitos

JDK 21

Maven 3.8+

Backend REST levantado en http://localhost:8080/api (puedes cambiarlo si es necesario).

La BD del backend creada con el script: Script tablas .sql.

🚀 Cómo correr

Levanta el backend (ver repo de la API).

Importa y ejecuta el archivo Script tablas .sql en tu gestor de BD.

Configura el application.properties/persistence.xml del backend según tu motor, usuario y contraseña.

Arranca el backend en http://localhost:8080.

Ejecuta el cliente (este repo):

mvn clean javafx:run


Si ves el warning “Loading FXML document with JavaFX API of version 17 by JavaFX runtime of version 13”, confirma que estás usando JDK 21 y que Maven ejecuta con ese JDK.

🔧 Configuración de la URL del API

La URL base del API se define en los services del cliente (por defecto http://localhost:8080/api).
Si tu backend corre en otro host/puerto, ajusta la constante base en:

src/main/java/Service/CitaService.java

src/main/java/Service/PacienteService.java

src/main/java/Service/ClinicaService.java

🔌 Endpoints útiles (cheat-sheet)
Citas (/api/citas)

GET /api/citas – listar

GET /api/citas/{id} – detalle

POST /api/citas – crear

PUT /api/citas/{id} – actualizar

DELETE /api/citas/{id} – eliminar

GET /api/citas/disponible?idClinica={id}&fecha={yyyy-MM-dd}&ini={HH:mm}&fin={HH:mm} – disponibilidad

Pacientes (/api/pacientes)

CRUD estándar.

Clínicas (/api/clinicas)

CRUD estándar.

Formato: la API usa yyyy-MM-dd para fechas y HH:mm para horas.
El cliente normaliza horas como 9:00 → 09:00.

🖱️ Uso rápido

En la vista Gestor de Citas, selecciona Paciente, Clínica, Fecha, ingresa Hora Inicio y Hora Fin (HH:mm) y Descripción.

Guardar para crear.

Selecciona una fila → Actualizar o Eliminar.

Usa los Filtros (texto, clínica y fecha) para buscar.

📁 Estructura del proyecto (cliente)
clinica/
├─ Script tablas .sql            # <-- Script de BD para el backend (ejecútalo allá)
├─ pom.xml
├─ src/
│  ├─ main/java/
│  │  ├─ controllers/            # CitaFormController, etc.
│  │  ├─ Service/                # CitaService/PacienteService/ClinicaService (HTTP)
│  │  └─ db/                     # POJOs JPA usados por Gson (IdPaciente, IdClinica, etc.)
│  └─ main/resources/
│     └─ fxml/                   # CitaForm.fxml y estilos
└─ README.md
