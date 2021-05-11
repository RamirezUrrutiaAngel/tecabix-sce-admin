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
package mx.tecabix.service.controller;

import java.util.HashMap;
import mx.tecabix.db.entity.Turno;
import mx.tecabix.service.TecabixService;
import mx.tecabix.service.page.TurnoPage;
import org.springframework.http.HttpMethod;

/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
public class TurnoController extends TecabixService<Turno, TurnoPage> {
    
    private static final String URL_TURNO = "turno/v1";
    private static final String ELEMENTS = "elements";
    private static final String PAGE = "page";
    private static final String NOMBRE = "NOMBRE";
    private static final String DESCRIPCION = "DESCRIPCION";
    private static final String CURP = "CURP";
    private static final String PUESTO = "PUESTO";
    private static final String PLANTEL = "PLANTEL";
    private static final String SEARCH = "search";
    private static final String BY = "by";
    private static final String CLAVE = "clave";
    public static final int BY_NOMBRE = 0;
    public static final int BY_DESCRIPCION = 1;    
    public TurnoController() {
        super(TurnoPage.class);
    }
    
    public TurnoPage find(String search, int by, byte elements, short page) throws Exception {
        HashMap<String, Object> arg = new HashMap<>();
        if (search != null) {
            if (!search.isEmpty()) {
                arg.put(SEARCH, search);
                switch (by) {
                    case BY_NOMBRE:
                        arg.put(BY, NOMBRE);
                        break;
                    case BY_DESCRIPCION:
                        arg.put(BY, DESCRIPCION);
                        break;
                }
            }
        }
        arg.put(ELEMENTS, elements);
        arg.put(PAGE, page);
        return getPeticion(HttpMethod.GET, URL_TURNO, arg);
    }
}
