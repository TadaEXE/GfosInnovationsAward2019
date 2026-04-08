/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

/**
 *
 * @author Henry
 */
public class DBManager
{
    public static void createInDB(Object o, EntityManager em)//Create object in DB
    {
        //Safty checks:
        if (!em.getTransaction().isActive())
        {
            em.getTransaction().begin();
            em.persist(o);//Actual saving
            em.getTransaction().commit();
        }
        else
        {
            em.getTransaction().rollback();//If fails then recover state from before transaction
        }
    }

    public static void saveInDB(Object o, EntityManager em)//Update object in DB
    {
        //Safty checks:
        if (!em.getTransaction().isActive())
        {
            em.getTransaction().begin();
            
            if (em.getLockMode(o) == LockModeType.NONE)
            {
                em.merge(o);//Actual updating
                
            }
            em.getTransaction().commit();
        }
        else
        {
            em.getTransaction().rollback();//If fails then recover state from before transaction
        }
    }

    public static boolean deleteFromDB(Object o, EntityManager em)//Delete object from DB
    {
        //Savety checks:
        if (!em.getTransaction().isActive())
        {
            em.getTransaction().begin();
            if (em.getLockMode(o) == LockModeType.NONE)
            {
                em.remove(o);//Actual removal
            }
            else
            {
                em.getTransaction().rollback();//If fails then recover state from before transaction
                return false;
            }
            em.getTransaction().commit();
        }
        else
        {
            em.getTransaction().rollback();//If fails then recover state from before transaction
            return false;
        }
        return true;
    }
}
