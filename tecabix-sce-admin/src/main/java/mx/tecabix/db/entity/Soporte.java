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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
@Entity()
public class Soporte implements Serializable {

    private static final long serialVersionUID = -7012345416547676841L;
    @Id
    private Long id;
    @Column(name = "id_empresa")
    private Long idEmpresa;
    @ManyToOne
    @JoinColumn(name = "id_tipo")
    private Catalogo tipo;
    @Column(name = "asunto")
    private String asunto;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "id_usuario_modificado")
    private Long idUsuarioModificado;
    @Column(name = "fecha_modificado")
    private LocalDateTime fechaDeModificacion;
    @ManyToOne
    @JoinColumn(name = "id_estatus")
    private Catalogo estatus;
    @Column(name = "clave")
    private UUID clave;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "soporte", cascade = CascadeType.REMOVE)
    private List<SoporteMsj> soporteMsjs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Catalogo getTipo() {
        return tipo;
    }

    public void setTipo(Catalogo tipo) {
        this.tipo = tipo;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public List<SoporteMsj> getSoporteMsjs() {
        return soporteMsjs;
    }

    public void setSoporteMsjs(List<SoporteMsj> soporteMsjs) {
        this.soporteMsjs = soporteMsjs;
    }
}
