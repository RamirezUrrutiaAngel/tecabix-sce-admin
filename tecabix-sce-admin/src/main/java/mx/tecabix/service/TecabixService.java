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
package mx.tecabix.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mx.tecabix.ConfiguracionConexion;
import mx.tecabix.db.entity.Autorizacion;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
/**
 * 
 * @author Ramirez Urrutia Angel Abinadi
 * 
 */
public class TecabixService<REQUEST,RESPONS> {
    private final ConfiguracionConexion conexion;
    private final Class clase;

    public TecabixService(Class claseResponse) {
        this.clase = claseResponse;
        conexion = ConfiguracionConexion.getConfiguracionConexion();
    }
    
    public RESPONS getPeticion(HttpMethod method, String url) throws Exception{
        return getPeticion(method, url, new HashMap<>(), null);
    }
    public RESPONS getPeticion(HttpMethod method, String url, REQUEST body) throws Exception{
        return getPeticion(method, url, new HashMap<>(), body);
    }
    public RESPONS getPeticion(HttpMethod method, String url, HashMap<String,Object> arg) throws Exception{
        return getPeticion(method, url, arg, null);
    }
    public RESPONS getPeticion(HttpMethod method, String url, HashMap<String,Object> arg, REQUEST body) throws Exception{
        StringBuilder dir = new StringBuilder();
        dir.append(conexion.getHttp())
        .append(conexion.getIp())
        .append(":")
        .append(conexion.getPuerto())
        .append("/")
        .append(url);
        arg = (HashMap<String, Object>) arg.clone();
        if(conexion.getToken() != null){
            if(!arg.containsKey(ConfiguracionConexion.TOKEN)){
                arg.put(ConfiguracionConexion.TOKEN, conexion.getToken());
            }
        }
        if(!arg.isEmpty()){
            dir.append("?");
            Iterator<Map.Entry<String,Object>> iterador = arg.entrySet().iterator();
            while (iterador.hasNext()) {
                Map.Entry<String, Object>  i = iterador.next();
                dir.append(i.getKey()).append("=").append(i.getValue());
                if(iterador.hasNext()){
                    dir.append("&");
                }
            }
            
        }
        String usuario = conexion.getUsuario();
        String password = conexion.getPassword();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(usuario, password);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> listMediaType = new ArrayList<>();
        listMediaType.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(listMediaType);
        
        HttpEntity<REQUEST> requestHttpEntity = new HttpEntity<REQUEST>(body,httpHeaders);
        RESPONS respons;
        ResponseEntity<RESPONS> responseResponseEntity = new RestTemplate().exchange(dir.toString(), method, requestHttpEntity, clase);
        respons = responseResponseEntity.getBody();
        return respons;
    }
//    
//    public RESPONS getPeticionBody(HttpMethod method, String url, HashMap<String,Object> arg) throws Exception{
//        StringBuilder dir = new StringBuilder();
//        dir.append(conexion.getHttp())
//        .append(conexion.getIp())
//        .append(":")
//        .append(conexion.getPuerto())
//        .append("/")
//        .append(url);
//        arg = (HashMap<String, Object>) arg.clone();
//        if(conexion.getToken() != null){
//            if(!arg.containsKey(ConfiguracionConexion.TOKEN)){
//                arg.put(ConfiguracionConexion.TOKEN, conexion.getToken());
//            }
//        }
//        if(!arg.isEmpty()){
//            dir.append("?");
//            Iterator<Map.Entry<String,Object>> iterador = arg.entrySet().iterator();
//            while (iterador.hasNext()) {
//                Map.Entry<String, Object>  i = iterador.next();
//                dir.append(i.getKey()).append("=").append(i.getValue());
//                if(iterador.hasNext()){
//                    dir.append("&");
//                }
//            }
//            
//        }
//        String usuario = conexion.getUsuario();
//        String password = conexion.getPassword();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setBasicAuth(usuario, password);
//        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        List<MediaType> listMediaType = new ArrayList<>();
//        listMediaType.add(MediaType.APPLICATION_JSON);
//        httpHeaders.setAccept(listMediaType);
//        
//        HttpEntity<Authority> requestHttpEntity = new HttpEntity<Authority>();
//        
//        RESPONS respons;
//        ResponseEntity<RESPONS> responseResponseEntity = new RestTemplate().exchange(dir.toString(), method, requestHttpEntity, clase);
//        respons = responseResponseEntity.getBody();
//        return respons;
//    }
//    
    
}
