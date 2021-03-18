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
import mx.tecabix.db.entity.Sesion;
import mx.tecabix.service.TecabixService;
import mx.tecabix.service.page.SesionPage;
import org.springframework.http.HttpMethod;


/**
 * 
 * @author Ramirez Urrutia Angel Abinadi
 * 
 */
public class SesionConreoller extends TecabixService<Sesion, SesionPage>{
    
    private static final String URL_SESION ="sesion/v1";
    private static final String URL_SESION_DELETE_ROOT ="sesion/v1/deleteRoot";
    
    
    private static final String KEY = "key";
    private static final String CLAVE = "clave";
    private static final String ELEMENTS = "elements";
    private static final String PAGE = "page";
    private static final String SEARCH = "search";
    private static final String BY = "by";
    private static final String USUARIO = "USUARIO";
    private static final String LICENCIA = "LICENCIA";
    private static final String SERVICIO = "SERVICIO";
    
    public static final int BY_USUARIO = 0;
    public static final int BY_LICENCIA = 1;
    public static final int BY_SERVICIO = 2;
    
    
    
    public SesionConreoller() {
        super(SesionPage.class);
    }
    
    public Sesion post(String key) throws Exception{
        TecabixService<?,Sesion> service = new TecabixService<>(Sesion.class);
        HashMap<String,Object> arg = new HashMap<>();
        arg.put(KEY,key);
        return service.getPeticion(HttpMethod.POST, URL_SESION, arg);
    }
    
    public Sesion delete() throws Exception{
        TecabixService<?,Sesion> service = new TecabixService<>(Sesion.class);
        return service.getPeticion(HttpMethod.DELETE, URL_SESION);
    }
    public Sesion delete(UUID clave) throws Exception{
        TecabixService<?,Sesion> service = new TecabixService<>(Sesion.class);
        HashMap<String,Object> arg = new HashMap<>();
        arg.put(CLAVE,clave);
        return service.getPeticion(HttpMethod.DELETE, URL_SESION_DELETE_ROOT,arg);
    }
    
    public SesionPage find(byte elements, short page)throws Exception{
        return find(null,0, elements, page);
    }
    
    public SesionPage find(String search, int by, byte elements, short page)throws Exception{
        
        HashMap<String,Object> arg = new HashMap<>();
        if(search != null){
            if(!search.isEmpty()){
                arg.put(SEARCH,search);
                switch (by) {
                    case BY_USUARIO:
                        arg.put(BY,USUARIO);
                        break;
                    case BY_LICENCIA:
                        arg.put(BY,LICENCIA);
                        break;
                    case BY_SERVICIO:
                        arg.put(BY,SERVICIO);
                        break;
                }
            }
        }
        arg.put(ELEMENTS,elements);
        arg.put(PAGE,page);
        return getPeticion(HttpMethod.GET, URL_SESION, arg);
    }
}
