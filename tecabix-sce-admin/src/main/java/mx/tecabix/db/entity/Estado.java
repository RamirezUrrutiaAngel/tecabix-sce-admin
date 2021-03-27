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
 * @author Ramirez Urrutia Angel Abinad
 */
@Entity
public class Estado implements Serializable {

    private static final long serialVersionUID = 8735982161199720818L;

    @Id
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "abreviatura")
    private String abreviatura;
    @Column(name = "id_usuario_modificado")
    private Long idUsuarioModificado;
    @Column(name = "fecha_modificado")
    private LocalDateTime fechaDeModificacion;
    @ManyToOne
    @JoinColumn(name = "id_estatus")
    private Catalogo estatus;
    @Column(name = "clave")
    private UUID clave;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadFederativa", cascade = CascadeType.REMOVE)
    private List<Municipio> municipios;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
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

    public List<Municipio> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(List<Municipio> municipios) {
        this.municipios = municipios;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clave == null) ? 0 : clave.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Estado other = (Estado) obj;
        if (clave == null) {
            if (other.clave != null) {
                return false;
            }
        } else if (!clave.equals(other.clave)) {
            return false;
        }
        return true;
    }
}
