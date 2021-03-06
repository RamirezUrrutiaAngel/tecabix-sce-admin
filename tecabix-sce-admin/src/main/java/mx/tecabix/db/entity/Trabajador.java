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
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.util.Objects;

/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
@Entity
public class Trabajador implements Serializable {

    private static final long serialVersionUID = -7407043110565368553L;

    public static final short SIZE_CURP = 19;
    public static final short SIZE_URL_IMG = 200;

    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_persona_fisica")
    private PersonaFisica personaFisica;
    @ManyToOne
    @JoinColumn(name = "id_seguro_social")
    private SeguroSocial seguroSocial;
    @ManyToOne
    @JoinColumn(name = "id_puesto")
    private Puesto puesto;
    @ManyToOne
    @JoinColumn(name = "id_plantel")
    private Plantel plantel;
    @ManyToOne
    @JoinColumn(name = "id_turno")
    private Turno turno;
    @ManyToOne
    @JoinColumn(name = "id_salario")
    private Salario salario;
    @ManyToOne
    @JoinColumn(name = "id_jefe")
    private Trabajador jefe;
    @Column(name = "id_empresa")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonaFisica getPersonaFisica() {
        return personaFisica;
    }

    public void setPersonaFisica(PersonaFisica personaFisica) {
        this.personaFisica = personaFisica;
    }

    public SeguroSocial getSeguroSocial() {
        return seguroSocial;
    }

    public void setSeguroSocial(SeguroSocial seguroSocial) {
        this.seguroSocial = seguroSocial;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    public Plantel getPlantel() {
        return plantel;
    }

    public void setPlantel(Plantel plantel) {
        this.plantel = plantel;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Salario getSalario() {
        return salario;
    }

    public void setSalario(Salario salario) {
        this.salario = salario;
    }

    public Trabajador getJefe() {
        return jefe;
    }

    public void setJefe(Trabajador jefe) {
        this.jefe = jefe;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.clave);
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
        final Trabajador other = (Trabajador) obj;
        if (!Objects.equals(this.clave, other.clave)) {
            return false;
        }
        return true;
    }
}
