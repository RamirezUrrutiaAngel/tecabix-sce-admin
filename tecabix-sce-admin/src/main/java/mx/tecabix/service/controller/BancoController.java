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
import mx.tecabix.db.entity.Banco;
import mx.tecabix.service.TecabixService;
import mx.tecabix.service.page.BancoPage;
import org.springframework.http.HttpMethod;


/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
public class BancoController extends TecabixService<Banco,BancoPage>{
    public BancoController() {
        super(BancoPage.class);
    }
    
    private static final String URL_BANCO ="banco/v1";
    
    private static final String ELEMENTS = "elements";
    private static final String PAGE = "page";
    private static final String NOMBRE = "NOMBRE";
    private static final String SEARCH = "search";
    private static final String BY = "by";
    private static final String CLAVE = "clave";       
    public static final int BY_NOMBRE = 0;
    
    public Banco save(Banco save)throws Exception{
        TecabixService<Banco,Banco> service = new TecabixService<>(Banco.class);
        return service.getPeticion(HttpMethod.POST, URL_BANCO, save);
    }
    
    public Banco update(Banco update)throws Exception{
        TecabixService<Banco,Banco> service = new TecabixService<>(Banco.class);
        return service.getPeticion(HttpMethod.PUT, URL_BANCO, update);
    }
    public void delete(UUID  clave)throws Exception{
        TecabixService<?,?> service = new TecabixService<>(Banco.class);
        HashMap<String,Object> arg = new HashMap<>();
        arg.put(CLAVE, clave);
        service.getPeticion(HttpMethod.DELETE, URL_BANCO,arg);
    }
    
    
    public BancoPage find(String search, int by, byte elements, short page)throws Exception{
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
        return getPeticion(HttpMethod.GET, URL_BANCO, arg);
    }
}
