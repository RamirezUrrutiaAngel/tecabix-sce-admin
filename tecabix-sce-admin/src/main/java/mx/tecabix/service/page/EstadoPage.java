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
package mx.tecabix.service.page;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

import mx.tecabix.db.entity.Estado;
import mx.tecabix.service.PageGeneric;

/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
public class EstadoPage extends PageGeneric implements Serializable {

    private static final long serialVersionUID = -3129044952862700224L;

    private List<Estado> data;

    public EstadoPage() {
    }

    public EstadoPage(Page<Estado> data) {
        super(data);
        this.data = data.getContent();
    }

    public List<Estado> getData() {
        return data;
    }

    public void setData(List<Estado> data) {
        this.data = data;
    }

}
