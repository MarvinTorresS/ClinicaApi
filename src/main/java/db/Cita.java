package db;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Date;

@javax.persistence.Entity
@javax.persistence.Table(name = "CITA")
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = "Cita.findAll", query = "SELECT c FROM Cita c"),
    @javax.persistence.NamedQuery(name = "Cita.findByIdCita", query = "SELECT c FROM Cita c WHERE c.idCita = :idCita"),
    @javax.persistence.NamedQuery(name = "Cita.findByFechaCita", query = "SELECT c FROM Cita c WHERE c.fechaCita = :fechaCita"),
    @javax.persistence.NamedQuery(name = "Cita.findByHoraInicio", query = "SELECT c FROM Cita c WHERE c.horaInicio = :horaInicio"),
    @javax.persistence.NamedQuery(name = "Cita.findByHoraFin", query = "SELECT c FROM Cita c WHERE c.horaFin = :horaFin"),
    @javax.persistence.NamedQuery(name = "Cita.findByEstado", query = "SELECT c FROM Cita c WHERE c.estado = :estado"),
    @javax.persistence.NamedQuery(name = "Cita.findByDescripcion", query = "SELECT c FROM Cita c WHERE c.descripcion = :descripcion")
})
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;

    @javax.persistence.Id
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "ID_CITA")
    private Long idCita;

    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "FECHA_CITA")
    @javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaCita;

    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "HORA_INICIO")
    private String horaInicio;

    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "HORA_FIN")
    private String horaFin;

    @javax.persistence.Column(name = "ESTADO")
    private String estado;

    @javax.persistence.Column(name = "DESCRIPCION")
    private String descripcion;

    @javax.persistence.JoinColumn(name = "ID_CLINICA", referencedColumnName = "ID_CLINICA")
    @javax.persistence.ManyToOne(optional = false)
    // ⬇⬇ JSON del backend usa "clinica", lo mapeamos a idClinica
    @SerializedName("clinica")
    private Clinica idClinica;

    @javax.persistence.JoinColumn(name = "ID_PACIENTE", referencedColumnName = "ID_PACIENTE")
    @javax.persistence.ManyToOne(optional = false)
    // ⬇⬇ JSON del backend usa "paciente", lo mapeamos a idPaciente
    @SerializedName("paciente")
    private Paciente idPaciente;

    public Cita() { }
    public Cita(Long idCita) { this.idCita = idCita; }
    public Cita(Long idCita, Date fechaCita, String horaInicio, String horaFin) {
        this.idCita = idCita; this.fechaCita = fechaCita; this.horaInicio = horaInicio; this.horaFin = horaFin;
    }

    public Long getIdCita() { return idCita; }
    public void setIdCita(Long idCita) { this.idCita = idCita; }
    public Date getFechaCita() { return fechaCita; }
    public void setFechaCita(Date fechaCita) { this.fechaCita = fechaCita; }
    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }
    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Clinica getIdClinica() { return idClinica; }
    public void setIdClinica(Clinica idClinica) { this.idClinica = idClinica; }
    public Paciente getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Paciente idPaciente) { this.idPaciente = idPaciente; }

    @Override public int hashCode() { return (idCita != null ? idCita.hashCode() : 0); }
    @Override public boolean equals(Object o) {
        if (!(o instanceof Cita)) return false;
        Cita other = (Cita) o;
        if (this.idCita == null && other.idCita != null) return false;
        return !(this.idCita != null && !this.idCita.equals(other.idCita));
    }
    @Override public String toString() { return "db.Cita[ idCita=" + idCita + " ]"; }
}
