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
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.util.Objects;
import javax.persistence.GeneratedValue;

/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
@Entity
public class Catalogo implements Serializable {

    private static final long serialVersionUID = 8898558749708373148L;

    public static final short SIZE_NOMBRE = 45;
    public static final short SIZE_DESCRIPCION = 500;
    public static final short SIZE_NOMBRE_COMPLETO = 250;

    @Id
    @GeneratedValue
    private long id;
    private String nombre;
    @JsonProperty(access = Access.WRITE_ONLY)
    private String nombreCompleto;
    private String descripcion;
    @JsonProperty(access = Access.WRITE_ONLY)
    private Integer orden;
    @JsonProperty(access = Access.WRITE_ONLY)
    @ManyToOne
    private CatalogoTipo catalogoTipo;
    private UUID clave;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public CatalogoTipo getCatalogoTipo() {
        return catalogoTipo;
    }

    public void setCatalogoTipo(CatalogoTipo catalogoTipo) {
        this.catalogoTipo = catalogoTipo;
    }

    public UUID getClave() {
        return this.clave;
    }

    public void setClave(UUID clave) {
        this.clave = clave;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.clave);
        return hash;
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
        final Catalogo other = (Catalogo) obj;
        if (!Objects.equals(this.clave, other.clave)) {
            return false;
        }
        return true;
    }
}
