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
import mx.tecabix.db.entity.Usuario;
import mx.tecabix.service.TecabixService;
import mx.tecabix.service.page.UsuarioPage;
import org.springframework.http.HttpMethod;

/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
public class UsuarioController extends TecabixService<Usuario,UsuarioPage>{
    
    public UsuarioController() {
        super(UsuarioPage.class);
    }
    
    private static final String URL_USUARIO ="usuario/v1";
    private static final String URL_IS_USERNAME_ACCEPTED ="usuario/v1/is-username-accepted";
    
    private static final String ELEMENTS = "elements";
    private static final String PAGE = "page";
    private static final String USERNAME = "username";
    private static final String NOMBRE = "NOMBRE";
    private static final String CORREO = "CORREO";
    private static final String PERFIL = "PERFIL";
    private static final String SEARCH = "search";
    private static final String BY = "by";
    private static final String CLAVE = "clave";       
    public static final int BY_NOMBRE = 0;
    public static final int BY_CORREO = 1;
    public static final int BY_PERFIL= 2;
    
    public Usuario save(Usuario save)throws Exception{
        TecabixService<Usuario,Usuario> service = new TecabixService<>(Usuario.class);
        return service.getPeticion(HttpMethod.POST, URL_USUARIO, save);
    }
    
    public Usuario update(Usuario update)throws Exception{
        TecabixService<Usuario,Usuario> service = new TecabixService<>(Usuario.class);
        return service.getPeticion(HttpMethod.PUT, URL_USUARIO, update);
    }
    public void delete(UUID  clave)throws Exception{
        TecabixService<?,?> service = new TecabixService<>(Usuario.class);
        HashMap<String,Object> arg = new HashMap<>();
        arg.put(CLAVE, clave);
        service.getPeticion(HttpMethod.DELETE, URL_USUARIO,arg);
    }
    
    
    public UsuarioPage find(String search, int by, byte elements, short page)throws Exception{
        HashMap<String,Object> arg = new HashMap<>();
        if(search != null){
            if(!search.isEmpty()){
                arg.put(SEARCH,search);
                switch (by) {
                    case BY_NOMBRE:
                        arg.put(BY,NOMBRE);
                        break;
                    case BY_CORREO:
                        arg.put(BY,CORREO);
                        break;
                    case BY_PERFIL:
                        arg.put(BY,PERFIL);
                        break;
                }
            }
        }
        arg.put(ELEMENTS,elements);
        arg.put(PAGE,page);
        return getPeticion(HttpMethod.GET, URL_USUARIO, arg);
    }
    public void exists(String usuario)throws Exception{
        HashMap<String,Object> arg = new HashMap<>();
        arg.put(USERNAME,usuario);
        getPeticion(HttpMethod.GET, URL_IS_USERNAME_ACCEPTED, arg);
    }
    
}
