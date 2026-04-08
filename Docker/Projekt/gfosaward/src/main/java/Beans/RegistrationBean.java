/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Util.DateConverter;
import Util.EntityFactory;
import Util.Hashcode;
import Util.InputValidation;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import org.primefaces.PrimeFaces;

/**
 *
 * 
 */
@RequestScoped
@ManagedBean(name = "registrationBean")
public class RegistrationBean
{
    private String name;
    private String lastName;
    private String mail;
    private String password;
    private String verifyPassword;
    private String birthDate;

//<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getLastName()
    {
        return lastName;
    }
    
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
    
    public String getMail()
    {
        return mail;
    }
    
    public void setMail(String mail)
    {
        this.mail = mail;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getVerifyPassword()
    {
        return verifyPassword;
    }
    
    public void setVerifyPassword(String verifyPassword)
    {
        this.verifyPassword = verifyPassword;
    }
    
    public String getBirthDate()
    {
        return birthDate;
    }
    
    public void setBirthDate(String birthDate)
    {
        this.birthDate = birthDate;
    }
    
    
//</editor-fold>
    
    public String register()//if user input is ok then 
    {
        if(validInput())
        {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getApplication().evaluateExpressionGet(context, "#{userBean}", UserBean.class)
                    .setCurrentUser(EntityFactory.createUserObject
                    ( name, lastName, mail,  DateConverter.stringToSqlDate(birthDate), Date.valueOf(LocalDate.now()), Hashcode.encrypt(password), em));
            return "Homescreen.xhtml?tab=Taskscreen&faces-redirect=true";
        }
        return "";
    }
    
    private boolean validInput()
    {
        List<String> falseSquares = new ArrayList<>();
        if(!InputValidation.validateName(name) || name.isEmpty())
        {
            falseSquares.add("square regNameErr");
        }
        if(!InputValidation.validateName(lastName) || lastName.isEmpty())
        {
            falseSquares.add("square regLastNameErr");
        }
        if(!InputValidation.validateEmail(mail) || mail.isEmpty())
        {
            falseSquares.add("square regMailName");
        }
        if(!InputValidation.validatePassword(password) || password.isEmpty())
        {
            falseSquares.add("square regPasswordErr");
        }
        if(!InputValidation.validatePassword(verifyPassword) || verifyPassword.isEmpty())
        {
            falseSquares.add("square regValidatePasswordErr");
        }
        if(!verifyPassword.equals(password))
        {
            falseSquares.add("square regValidatePasswordErr");
        }
        if(!InputValidation.validateDate(birthDate) || birthDate.isEmpty())
        {
            falseSquares.add("square regBirthDatErr");
        }
        
        if(!falseSquares.isEmpty())
        {
            for(String s : falseSquares)
            {
                PrimeFaces.current().executeScript("errorSquare('"+s+"', true)");
            }
            return false;
        }
        
        return true;
    }
    
    private EntityManager em;
    
    @PostConstruct
    public void init()
    {
        em = MainBean.getEm();
    }
}
