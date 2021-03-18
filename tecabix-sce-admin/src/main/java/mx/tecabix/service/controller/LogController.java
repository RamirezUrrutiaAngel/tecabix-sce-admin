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
import mx.tecabix.service.TecabixService;
import org.springframework.http.HttpMethod;

/**
 * 
 * @author Ramirez Urrutia Angel Abinadi
 * 
 */
public class LogController extends TecabixService{
    private static final String URL_LOG ="log/v1";
    private static final String CUERPO = "cuerpo";
    public LogController() {
        super(Object.class);
    }
    
    public Object post(String cuerpo) throws Exception{
        HashMap<String,Object> arg = new HashMap<>();
        arg.put(CUERPO,cuerpo);
        return getPeticion(HttpMethod.POST, URL_LOG, arg);
    }
    
}
