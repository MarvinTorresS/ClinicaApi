/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Marvin
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "PACIENTE")
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = "Paciente.findAll", query = "SELECT p FROM Paciente p"),
    @javax.persistence.NamedQuery(name = "Paciente.findByIdPaciente", query = "SELECT p FROM Paciente p WHERE p.idPaciente = :idPaciente"),
    @javax.persistence.NamedQuery(name = "Paciente.findByNombre", query = "SELECT p FROM Paciente p WHERE p.nombre = :nombre"),
    @javax.persistence.NamedQuery(name = "Paciente.findByApellido", query = "SELECT p FROM Paciente p WHERE p.apellido = :apellido"),
    @javax.persistence.NamedQuery(name = "Paciente.findByFechaNac", query = "SELECT p FROM Paciente p WHERE p.fechaNac = :fechaNac"),
    @javax.persistence.NamedQuery(name = "Paciente.findByTelefono", query = "SELECT p FROM Paciente p WHERE p.telefono = :telefono"),
    @javax.persistence.NamedQuery(name = "Paciente.findByCorreo", query = "SELECT p FROM Paciente p WHERE p.correo = :correo"),
    @javax.persistence.NamedQuery(name = "Paciente.findByFechaRegistro", query = "SELECT p FROM Paciente p WHERE p.fechaRegistro = :fechaRegistro")})
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;
    @javax.persistence.Id
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "ID_PACIENTE")
    private Long idPaciente;
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "NOMBRE")
    private String nombre;
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "APELLIDO")
    private String apellido;
    @javax.persistence.Column(name = "FECHA_NAC")
    @javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaNac;
    @javax.persistence.Column(name = "TELEFONO")
    private String telefono;
    @javax.persistence.Column(name = "CORREO")
    private String correo;
    @javax.persistence.Column(name = "FECHA_REGISTRO")
    @javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "idPaciente")
    private Collection<Cita> citaCollection;

    public Paciente() {
    }

    public Paciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Paciente(Long idPaciente, String nombre, String apellido) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Collection<Cita> getCitaCollection() {
        return citaCollection;
    }

    public void setCitaCollection(Collection<Cita> citaCollection) {
        this.citaCollection = citaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPaciente != null ? idPaciente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paciente)) {
            return false;
        }
        Paciente other = (Paciente) object;
        if ((this.idPaciente == null && other.idPaciente != null) || (this.idPaciente != null && !this.idPaciente.equals(other.idPaciente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.Paciente[ idPaciente=" + idPaciente + " ]";
    }
    
}
