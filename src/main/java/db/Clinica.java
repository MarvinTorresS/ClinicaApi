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
@javax.persistence.Table(name = "CLINICA")
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = "Clinica.findAll", query = "SELECT c FROM Clinica c"),
    @javax.persistence.NamedQuery(name = "Clinica.findByIdClinica", query = "SELECT c FROM Clinica c WHERE c.idClinica = :idClinica"),
    @javax.persistence.NamedQuery(name = "Clinica.findByNombre", query = "SELECT c FROM Clinica c WHERE c.nombre = :nombre"),
    @javax.persistence.NamedQuery(name = "Clinica.findByDireccion", query = "SELECT c FROM Clinica c WHERE c.direccion = :direccion"),
    @javax.persistence.NamedQuery(name = "Clinica.findByTelefono", query = "SELECT c FROM Clinica c WHERE c.telefono = :telefono"),
    @javax.persistence.NamedQuery(name = "Clinica.findByCorreo", query = "SELECT c FROM Clinica c WHERE c.correo = :correo"),
    @javax.persistence.NamedQuery(name = "Clinica.findByFechaRegistro", query = "SELECT c FROM Clinica c WHERE c.fechaRegistro = :fechaRegistro")})
public class Clinica implements Serializable {

    private static final long serialVersionUID = 1L;
    @javax.persistence.Id
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "ID_CLINICA")
    private Long idClinica;
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "NOMBRE")
    private String nombre;
    @javax.persistence.Column(name = "DIRECCION")
    private String direccion;
    @javax.persistence.Column(name = "TELEFONO")
    private String telefono;
    @javax.persistence.Column(name = "CORREO")
    private String correo;
    @javax.persistence.Column(name = "FECHA_REGISTRO")
    @javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "idClinica")
    private Collection<Cita> citaCollection;

    public Clinica() {
    }

    public Clinica(Long idClinica) {
        this.idClinica = idClinica;
    }

    public Clinica(Long idClinica, String nombre) {
        this.idClinica = idClinica;
        this.nombre = nombre;
    }

    public Long getIdClinica() {
        return idClinica;
    }

    public void setIdClinica(Long idClinica) {
        this.idClinica = idClinica;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
        hash += (idClinica != null ? idClinica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clinica)) {
            return false;
        }
        Clinica other = (Clinica) object;
        if ((this.idClinica == null && other.idClinica != null) || (this.idClinica != null && !this.idClinica.equals(other.idClinica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.Clinica[ idClinica=" + idClinica + " ]";
    }
    
}
