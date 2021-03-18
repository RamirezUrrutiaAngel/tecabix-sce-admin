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

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
/**
 * 
 * @author Ramirez Urrutia Angel Abinadi
 * 
 */
public class ConfiguracionPropertie {
    private static String HASH_EQUIPO="hashEquipo";
    private static String HTTP="http";
    private static String IP="ip";
    private static String PUERTO="puerto";
    
    private static ConfiguracionPropertie configuracionPropertie;    
    private static final File URL = new File("configuracion.txt");
    private static final String MSJ_ERR_PERMISOS = "Error de permisos en la configuacion";
    private static final String MSJ_ERR_CONFI = "El Equipoo no esta correctamente configuarado";

    private ConfiguracionPropertie() {
    }

    public static ConfiguracionPropertie getConfiguracionPropertie() {
        if(configuracionPropertie == null){
            configuracionPropertie = new ConfiguracionPropertie();
        }
        return configuracionPropertie;
    }

    public String getHashEquipo() throws Exception{
        Properties properties = new Properties();
        String value;
        if(!URL.exists()){
            throw new Exception(MSJ_ERR_CONFI);
        }
        if(!URL.canRead()){
             throw new Exception(MSJ_ERR_PERMISOS);
        }
        try (FileReader fileReader = new FileReader(URL)) {
            properties.load(fileReader);
            value = properties.getProperty(HASH_EQUIPO);
            fileReader.close();
        }
        if(value == null || value.isEmpty()){
            throw new Exception(MSJ_ERR_CONFI);
        }
        return value;
    }
    
    public String getHttp() throws Exception{
        Properties properties = new Properties();
        String value;
        if(!URL.exists()){
            throw new Exception(MSJ_ERR_CONFI);
        }
        if(!URL.canRead()){
             throw new Exception(MSJ_ERR_PERMISOS);
        }
        try (FileReader fileReader = new FileReader(URL)) {
            properties.load(fileReader);
            value = properties.getProperty(HTTP);
            fileReader.close();
        }
        if(value == null || value.isEmpty()){
            throw new Exception(MSJ_ERR_CONFI);
        }
        return value;
    }
    
    public String getIP() throws Exception{
        Properties properties = new Properties();
        String value;
        if(!URL.exists()){
            throw new Exception(MSJ_ERR_CONFI);
        }
        if(!URL.canRead()){
             throw new Exception(MSJ_ERR_PERMISOS);
        }
        try (FileReader fileReader = new FileReader(URL)) {
            properties.load(fileReader);
            value = properties.getProperty(IP);
            fileReader.close();
        }
        if(value == null || value.isEmpty()){
            throw new Exception(MSJ_ERR_CONFI);
        }
        return value;
    }
    
    public String getPuerto() throws Exception{
        Properties properties = new Properties();
        String value;
        if(!URL.exists()){
            throw new Exception(MSJ_ERR_CONFI);
        }
        if(!URL.canRead()){
             throw new Exception(MSJ_ERR_PERMISOS);
        }
        try (FileReader fileReader = new FileReader(URL)) {
            properties.load(fileReader);
            value = properties.getProperty(PUERTO);
            fileReader.close();
        }
        if(value == null || value.isEmpty()){
            throw new Exception(MSJ_ERR_CONFI);
        }
        return value;
    }
}
