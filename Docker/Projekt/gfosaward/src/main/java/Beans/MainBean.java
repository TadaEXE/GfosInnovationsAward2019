/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;



/**
 *
 * @author Henry
 */
@ApplicationScoped
@ManagedBean(eager = true,name = "mainBean")
public class MainBean
{
    private static EntityManager em;
    private EntityManagerFactory emf;
    

    @PostConstruct
    public void init()
    {
        try
        {
            System.out.println("-----------------------------Setting up connection-------------------------------------");
            
            emf = Persistence.createEntityManagerFactory("psu");
            em = emf.createEntityManager();

        }
        catch (Exception ex)
        {
            Logger.getLogger(MainBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   

    @PreDestroy
    public void abortConnection()
    {
        System.out.println("------------------------------Closing connection-----------------------------------------");

        if (MainBean.em != null)
        {
            MainBean.em.close();
        }
        if (this.emf != null)
        {
            emf.close();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getter/Setter">
    public static EntityManager getEm()
    {
        return em;
    }
    //</editor-fold>
}
