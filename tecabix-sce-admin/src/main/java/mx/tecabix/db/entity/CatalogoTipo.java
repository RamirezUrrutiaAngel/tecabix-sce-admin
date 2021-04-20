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
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.util.Objects;
import javax.persistence.GeneratedValue;

/**
 *
 * @author Ramirez Urrutia Angel Abinad
 */
@Entity
public class CatalogoTipo implements Serializable {

    private static final long serialVersionUID = -4174323806062618433L;

    public static final short SIZE_NOMBRE = 45;
    public static final short SIZE_DESCRIPCION = 250;

    @Id
    @GeneratedValue
    private long id;
    private String nombre;
    private String descripcion;
    private String clave;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "catalogoTipo", cascade = CascadeType.REMOVE)
    private List<Catalogo> catalogos;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public UUID getClave() {
        return UUID.fromString(clave);
    }

    public void setClave(UUID clave) {
        this.clave = clave.toString();
    }

    public List<Catalogo> getCatalogos() {
        return catalogos;
    }

    public void setCatalogos(List<Catalogo> catalogos) {
        this.catalogos = catalogos;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.clave);
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
        final CatalogoTipo other = (CatalogoTipo) obj;
        if (!Objects.equals(this.clave, other.clave)) {
            return false;
        }
        return true;
    }
    
}
