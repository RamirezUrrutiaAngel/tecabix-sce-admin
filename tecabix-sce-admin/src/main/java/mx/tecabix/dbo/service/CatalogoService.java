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
package mx.tecabix.dbo.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import mx.tecabix.db.entity.Catalogo;
import mx.tecabix.db.entity.CatalogoTipo;
import mx.tecabix.dbo.DataBaseJPA;
/**
 * 
 * @author Ramirez Urrutia Angel Abinadi
 * 
 */
public class CatalogoService extends DataBaseJPA{
    
    private static final String SQL_FIND_NOMBRE ="SELECT c FROM CatalogoTipo c WHERE c.nombre = :nombre";
    
    public CatalogoTipo persist(CatalogoTipo save){
        List<Catalogo> list = save.getCatalogos();
        if(list == null){
            list = new ArrayList<>();
        }
        EntityManager em = conectar();
        em.getTransaction().begin();
        em.persist(save);
        for (Catalogo cata : list) {
            if(!em.contains(cata)){
                em.persist(cata);
                cata.setCatalogoTipo(save);
            }
        }
        em.getTransaction().commit();
        
        em.close();
        return save;        
    }
    
    public CatalogoTipo findByNombre(String nombre){
        try {
            EntityManager em = conectar();
            TypedQuery<CatalogoTipo> tq = em.createQuery(SQL_FIND_NOMBRE, CatalogoTipo.class).setParameter("nombre", nombre);
            return tq.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
