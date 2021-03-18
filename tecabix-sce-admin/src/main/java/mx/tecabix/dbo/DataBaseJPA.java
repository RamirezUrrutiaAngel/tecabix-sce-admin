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
package mx.tecabix.dbo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
/**
 * 
 * @author Ramirez Urrutia Angel Abinadi
 * 
 */
public class DataBaseJPA {
    private final  EntityManagerFactory entityManagerFactory;

    public DataBaseJPA() {
        entityManagerFactory = Persistence.createEntityManagerFactory(
          "objectdb:$objectdb/db/tecabix.odb");
    }
    
    public EntityManager conectar(){
        return entityManagerFactory.createEntityManager();
    }
    
}
