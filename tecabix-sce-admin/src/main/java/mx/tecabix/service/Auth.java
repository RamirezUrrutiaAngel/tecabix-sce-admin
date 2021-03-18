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

import java.util.List;
import java.util.regex.Pattern;
import mx.tecabix.ConfiguracionConexion;
import mx.tecabix.db.entity.Authority;
import mx.tecabix.db.entity.Catalogo;
import mx.tecabix.db.entity.Perfil;

import mx.tecabix.db.entity.Sesion;
import mx.tecabix.db.entity.Usuario;

/**
 *
 * @author Ramirez Urrutia Angel Abinadi
 *
 */
public class Auth {

    public boolean isAuthorized(String authority) {
        ConfiguracionConexion conexion = ConfiguracionConexion.getConfiguracionConexion();
        Sesion sesion = conexion.getSesion();
        if (sesion == null) {
            return false;
        }
        Usuario usuario = sesion.getUsuario();
        if (usuario == null) {
            return false;
        }
        Perfil perfil = usuario.getPerfil();
        if (perfil == null) {
            return false;
        }
        List<Authority> authoritys = perfil.getAuthorities();
        if (authoritys == null) {
            return false;
        }
        for (Authority item : authoritys) {
            if (item.getNombre() == null) {
                continue;
            }
            if (item.getNombre().equalsIgnoreCase(authority)) {
                Catalogo estatus = item.getEstatus();
                if(estatus == null){
                    continue;
                }
                if(estatus.getNombre().equalsIgnoreCase("ACTIVO")){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isNotAuthorized(String authority) {
        return !isAuthorized(authority);
    }

    public static final byte TIPO_OBJECT = -1;
    public static final byte TIPO_ALFA = 0;
    public static final byte TIPO_ALFA_NUMERIC = 1;
    public static final byte TIPO_ALFA_NUMERIC_SPACE = 2;
    public static final byte TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS = 3;
    public static final byte TIPO_TEL = 4;
    public static final byte TIPO_EMAIL = 5;
    public static final byte TIPO_NUMERIC = 6;
    public static final byte TIPO_VARIABLE = 7;
    public static final byte TIPO_NUMERIC_SPACE = 8;
    public static final byte TIPO_NUMERIC_NATURAL = 9;
    public static final byte TIPO_NUMERIC_POSITIVO = 10;
    public static final byte TIPO_NUMERIC_NEGATIVO = 11;

    private static final String ALFA = "[a-zA-Z[áéíóúÁÉÍÓÚñÑ]]+";
    private static final String ALFA_NUMERIC = "[a-zA-Z0-9[áéíóúÁÉÍÓÚñÑ]]+";
    private static final String ALFA_NUMERIC_SPACE = "[a-zA-Z0-9[áéíóúÁÉÍÓÚñÑ\\s]]+";
    private static final String ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS = "[a-zA-Z0-9[.,():¿?!¡_&%$#@|áéíóúÁÉÍÓÚñÑ\\s]]+";
    private static final String TEL = "([(]{1}[0-9]{2,3}[)]{1}[\\s]{1}){0,1}([0-9]{2,4}[\\s])*[0-9]{2,8}";
    private static final String EMAIL = "[a-zA-Z0-9]{1}[a-zA-Z0-9[._]]*[a-zA-Z0-9]{1}[@]{1}[a-zA-Z]{1}[a-zA-Z0-9]+([.]{1}[a-zA-Z]{2,4}){1,2}";
    private static final String VARIABLE = "[a-zA-Z]+([_]{1}[a-zA-Z0-9]+)*[a-zA-Z0-9]+";
    private static final String NUMERIC = "[0-9]+";
    private static final String NUMERIC_SPACE = "[0-9]+[0-9[\\s]]*[0-9]+";

    public boolean isNotValid(Object arg) {
        return isNotValid(TIPO_OBJECT, Integer.MAX_VALUE, arg);
    }

    public boolean isValid(Object arg) {
        return isValid(TIPO_OBJECT, Integer.MAX_VALUE, arg);
    }

    public boolean isNotValid(int size, Object arg) {
        return isNotValid(TIPO_OBJECT, size, arg);
    }

    public boolean isValid(int size, Object arg) {
        return isValid(TIPO_OBJECT, size, arg);
    }

    public boolean isNotValid(byte tipo, int size, Object arg) {
        return !isValid(tipo, size, arg);
    }

    public boolean isValid(byte tipo, int size, Object arg) {
        if (arg == null) {
            return false;
        }
        if (arg.getClass().equals(String.class) || arg.getClass().equals(StringBuilder.class)) {
            String text = arg.toString();
            if (text.trim().isEmpty() || text.length() > size) {
                return false;
            }
            if (tipo == TIPO_ALFA) {
                return Pattern.matches(ALFA, text);
            } else if (tipo == TIPO_ALFA_NUMERIC) {
                return Pattern.matches(ALFA_NUMERIC, text);
            } else if (tipo == TIPO_ALFA_NUMERIC_SPACE) {
                return Pattern.matches(ALFA_NUMERIC_SPACE, text);
            } else if (tipo == TIPO_ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS) {
                return Pattern.matches(ALFA_NUMERIC_SPACE_WITH_SPECIAL_SYMBOLS, text);
            } else if (tipo == TIPO_TEL) {
                return Pattern.matches(TEL, text);
            } else if (tipo == TIPO_EMAIL) {
                return Pattern.matches(EMAIL, text);
            } else if (tipo == TIPO_NUMERIC) {
                return Pattern.matches(NUMERIC, text);
            } else if (tipo == TIPO_NUMERIC_SPACE) {
                return Pattern.matches(NUMERIC_SPACE, text);
            } else if (tipo == TIPO_VARIABLE) {
                return Pattern.matches(VARIABLE, text);
            }
        } else if (arg.getClass().equals(Integer.class) || arg.getClass().equals(Long.class)) {
            Long num = Long.parseLong(arg.toString());
            if (tipo == TIPO_NUMERIC_NATURAL) {
                return num > 0;
            } else if (tipo == TIPO_NUMERIC_POSITIVO) {
                return num > -1;
            } else if (tipo == TIPO_NUMERIC_NEGATIVO) {
                return num < 0;
            }
        } else if (arg.getClass().equals(Float.class) || arg.getClass().equals(Double.class)) {
            Double num = Double.parseDouble(arg.toString());
            if (tipo == TIPO_NUMERIC_NATURAL) {
                long aux = num.longValue();
                return num > 0 && aux == num.doubleValue();
            } else if (tipo == TIPO_NUMERIC_POSITIVO) {
                return num > -1;
            } else if (tipo == TIPO_NUMERIC_NEGATIVO) {
                return num < 0;
            }
        }
        return true;
    }
}
