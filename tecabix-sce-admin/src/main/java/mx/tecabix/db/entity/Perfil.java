/*
 *   This file is part of Foobar.
 *
 *   Foobar is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Foobar is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package mx.tecabix.db.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
@Entity()
public class Perfil implements Serializable {

    private static final long serialVersionUID = 1945352087628007583L;
    
    public static final short SIZE_NOMBRE = 30;
    public static final short SIZE_DESCRIPCION = 250;
    
    @Id
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "id_empresa")
    @JsonProperty(access = Access.WRITE_ONLY)
    private Long idEmpresa;
    @Column(name = "id_usuario_modificado")
    @JsonProperty(access = Access.WRITE_ONLY)
    private Long idUsuarioModificado;
    @Column(name = "fecha_modificado")
    @JsonProperty(access = Access.WRITE_ONLY)
    private LocalDateTime fechaDeModificacion;
    @ManyToOne
    @JoinColumn(name = "id_estatus")
    private Catalogo estatus;
    @Column(name = "clave")
    private UUID clave;
    @ManyToMany(cascade = {CascadeType.REMOVE})
    @JoinTable(
            name = "perfil_authority",
            joinColumns = {
                @JoinColumn(name = "id_perfil")},
            inverseJoinColumns = {
                @JoinColumn(name = "id_authority")}
    )
    private List<Autorizacion> autorizaciones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Long getIdUsuarioModificado() {
        return idUsuarioModificado;
    }

    public void setIdUsuarioModificado(Long idUsuarioModificado) {
        this.idUsuarioModificado = idUsuarioModificado;
    }

    public LocalDateTime getFechaDeModificacion() {
        return fechaDeModificacion;
    }

    public void setFechaDeModificacion(LocalDateTime fechaDeModificacion) {
        this.fechaDeModificacion = fechaDeModificacion;
    }

    public Catalogo getEstatus() {
        return estatus;
    }

    public void setEstatus(Catalogo estatus) {
        this.estatus = estatus;
    }

    public UUID getClave() {
        return clave;
    }

    public void setClave(UUID clave) {
        this.clave = clave;
    }

    public List<Autorizacion> getAutorizaciones() {
        return autorizaciones;
    }

    public void setAutorizaciones(List<Autorizacion> authorities) {
        this.autorizaciones = authorities;
    }
}
