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

import mx.tecabix.db.entity.Autorizacion;
import mx.tecabix.service.TecabixService;
import org.springframework.http.HttpMethod;

/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
public class AuthorityController extends TecabixService<Autorizacion,Autorizacion>{
    
    private static final String URL_FIND_AUTENTIFICADOS ="authority/v1/findAutentificados";
    
    
    public AuthorityController() {
        super(Autorizacion.class);
    }
    
    public Autorizacion findAutentificados()throws Exception{
        return getPeticion(HttpMethod.GET, URL_FIND_AUTENTIFICADOS);
    }
    
}
