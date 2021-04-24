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
import java.util.UUID;
import mx.tecabix.db.entity.Trabajador;
import mx.tecabix.service.TecabixService;
import mx.tecabix.service.page.TrabajadorPage;
import org.springframework.http.HttpMethod;

/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
public class TrabajadorController extends TecabixService<Trabajador, TrabajadorPage> {

    private static final String URL_TRABAJADOR = "trabajador/v1";
    private static final String URL_FIND_BOSS = "trabajador/v1/find-boss";
    private static final String URL_FIND_BY_CLAVE = "trabajador/v1/find-by-clave";
    private static final String URL_FIND_BY_BOSS = "trabajador/v1/find-by-jefe";
    private static final String ELEMENTS = "elements";
    private static final String PAGE = "page";
    private static final String NOMBRE = "NOMBRE";
    private static final String APELLIDO_PATERNO = "APELLIDO_PATERNO";
    private static final String APELLIDO_MATERNO = "APELLIDO_MATERNO";
    private static final String CURP = "CURP";
    private static final String PUESTO = "PUESTO";
    private static final String PLANTEL = "PLANTEL";
    private static final String SEARCH = "search";
    private static final String BY = "by";
    private static final String CLAVE = "clave";
    public static final int BY_NOMBRE = 0;
    private static final int BY_APELLIDO_PATERNO = 1;
    private static final int BY_APELLIDO_MATERNO = 2;
    private static final int BY_CURP = 3;
    private static final int BY_PUESTO = 4;
    private static final int BY_PLANTEL = 5;

    public TrabajadorController() {
        super(TrabajadorPage.class);
    }

    public TrabajadorPage find(String search, int by, byte elements, short page) throws Exception {
        HashMap<String, Object> arg = new HashMap<>();
        if (search != null) {
            if (!search.isEmpty()) {
                arg.put(SEARCH, search);
                switch (by) {
                    case BY_NOMBRE:
                        arg.put(BY, NOMBRE);
                        break;
                    case BY_APELLIDO_PATERNO:
                        arg.put(BY, APELLIDO_PATERNO);
                        break;
                    case BY_APELLIDO_MATERNO:
                        arg.put(BY, APELLIDO_MATERNO);
                        break;
                    case BY_CURP:
                        arg.put(BY, CURP);
                        break;
                    case BY_PUESTO:
                        arg.put(BY, PUESTO);
                        break;
                    case BY_PLANTEL:
                        arg.put(BY, PLANTEL);
                        break;
                }
            }
        }
        arg.put(ELEMENTS, elements);
        arg.put(PAGE, page);
        return getPeticion(HttpMethod.GET, URL_TRABAJADOR, arg);
    }

    public Trabajador findBoss(UUID clave) throws Exception {
        HashMap<String, Object> arg = new HashMap<>();
        TecabixService<Trabajador, Trabajador> service = new TecabixService<>(Trabajador.class);
        arg.put(CLAVE, clave);
        return service.getPeticion(HttpMethod.GET, URL_FIND_BOSS, arg);
    }

    public Trabajador findByClave(UUID clave) throws Exception {
        HashMap<String, Object> arg = new HashMap<>();
        TecabixService<Trabajador, Trabajador> service = new TecabixService<>(Trabajador.class);
        arg.put(CLAVE, clave);
        return service.getPeticion(HttpMethod.GET, URL_FIND_BY_CLAVE, arg);
    }

    public TrabajadorPage findByJefe(UUID clave, byte elements, short page) throws Exception {
        HashMap<String, Object> arg = new HashMap<>();
        arg.put(CLAVE, clave);
        arg.put(ELEMENTS, elements);
        arg.put(PAGE, page);
        return getPeticion(HttpMethod.GET, URL_FIND_BY_BOSS, arg);
    }

    public Trabajador save(Trabajador save) throws Exception {
        TecabixService<Trabajador, Trabajador> service = new TecabixService<>(Trabajador.class);
        return service.getPeticion(HttpMethod.POST, URL_TRABAJADOR, save);
    }
}
