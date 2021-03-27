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
import mx.tecabix.db.entity.Municipio;
import mx.tecabix.service.TecabixService;
import mx.tecabix.service.page.MunicipioPage;
import org.springframework.http.HttpMethod;

/**
 * 
 * @author Ramirez Urrutia Angel Abinadi
 * 
 */
public class MunicipioController extends TecabixService<Municipio, MunicipioPage>{
    
    private static final String URL_MUNICIPIO ="municipio/v1";
    private static final String FIND_BY_ESTADO_CLAVE ="municipio/v1/find-by-estado-clave";
    
    private static final String CLAVE = "clave";
    private static final String ELEMENTS = "elements";
    private static final String PAGE = "page";
    private static final String NOMBRE = "NOMBRE";
    private static final String SEARCH = "search";
    private static final String BY = "by";     
    public static final int BY_NOMBRE = 0;
    
    public MunicipioController() {
        super(MunicipioPage.class);
    }
    
    public MunicipioPage find(String search, int by, byte elements, short page)throws Exception{
        HashMap<String,Object> arg = new HashMap<>();
        if(search != null){
            if(!search.isEmpty()){
                arg.put(SEARCH,search);
                switch (by) {
                    case BY_NOMBRE:
                        arg.put(BY,NOMBRE);
                        break;
                }
            }
        }
        arg.put(ELEMENTS,elements);
        arg.put(PAGE,page);
        return getPeticion(HttpMethod.GET, URL_MUNICIPIO, arg);
    }
    
    public MunicipioPage findByEstadoClave(UUID estadoClave)throws Exception{
        HashMap<String,Object> arg = new HashMap<>();
        arg.put(CLAVE,estadoClave);
        return getPeticion(HttpMethod.GET, FIND_BY_ESTADO_CLAVE, arg);
    }
}
