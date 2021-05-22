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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
@Entity()
public class PerfilAuthority implements Serializable {

    private static final long serialVersionUID = -4911315157724285906L;
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_perfil")
    private Perfil perfil;
    @ManyToOne
    @JoinColumn(name = "id_authority")
    private Autorizacion authority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Autorizacion getAuthority() {
        return authority;
    }

    public void setAuthority(Autorizacion authority) {
        this.authority = authority;
    }
}
