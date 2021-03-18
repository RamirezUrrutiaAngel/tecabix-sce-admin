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
package mx.tecabix;

import java.time.LocalDateTime;
import java.util.UUID;
import mx.tecabix.db.entity.Sesion;
/**
 * 
 * @author Ramirez Urrutia Angel Abinadi
 * 
 */
public class ConfiguracionConexion {
    public static String TOKEN = "token";
    private static ConfiguracionConexion configuracionConexion;
    private String usuario;
    private String password;
    private UUID token;
    private LocalDateTime expira;
    private String ip;
    private String puerto;
    private String http;
    private Sesion sesion;
    
    public static ConfiguracionConexion getConfiguracionConexion() {
        if(configuracionConexion == null){
            configuracionConexion = new ConfiguracionConexion();
        }
        return configuracionConexion;
    }

    private ConfiguracionConexion() {
    }

    public static String getTOKEN() {
        return TOKEN;
    }

    public static void setTOKEN(String TOKEN) {
        ConfiguracionConexion.TOKEN = TOKEN;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public LocalDateTime getExpira() {
        return expira;
    }

    public void setExpira(LocalDateTime expira) {
        this.expira = expira;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getHttp() {
        return http;
    }

    public void setHttp(String http) {
        this.http = http;
    }

    public Sesion getSesion() {
        return sesion;
    }

    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

   
    
}
