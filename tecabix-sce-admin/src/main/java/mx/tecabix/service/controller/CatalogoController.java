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
import mx.tecabix.db.entity.Catalogo;
import mx.tecabix.db.entity.CatalogoTipo;
import mx.tecabix.service.TecabixService;
import org.springframework.http.HttpMethod;
/**
 * 
 * @author Ramirez Urrutia Angel Abinadi
 * 
 */
public class CatalogoController extends TecabixService<Catalogo,CatalogoTipo>{
    
    private static final String URL_FIND_BY_TIPO_NOMBRE  ="catalogo/v1/findByTipoNombre";
    
    private static final String NOMBRE_TIPO_NOMBRE ="catalogoTipoNombre";
     
    public CatalogoController() {
        super(CatalogoTipo.class);
    }
    public CatalogoTipo findByTipoNombre(String nombre)throws Exception{
        HashMap<String,Object> arg = new HashMap<>();
        arg.put(NOMBRE_TIPO_NOMBRE,nombre);
        return getPeticion(HttpMethod.GET, URL_FIND_BY_TIPO_NOMBRE,arg);
    }
    
}
