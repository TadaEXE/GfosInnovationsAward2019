/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Beans.MainBean;
import Tables.User;
import java.time.format.DateTimeParseException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * [^@.,!"§$%&/()=?´`+*~#'-_:;<>|\\\[\]\^°0-9] alle sonder zeichen
 *
 * @author Henry
 */
public class InputValidation
{

    public static boolean validateName(String input)//check if a name is valid (in german)
    {
        if(input == null || input.length() > 15)
            return false;
        return (input.matches("[a-zA-ZöÖüÜäÄß]+")); 
    }

    public static boolean validatePassword(String input)//check if a password meets the requierments
    {
        if(input == null)
            return false;
        return ((input.matches("^.{8,16}$")));
    }

    public static boolean validateDate(String input)//check if a date is written in the german pattern
    {
        if(input == null)
            return false;
        boolean valid = false;
        try
        {
            DateConverter.stringToSqlDate(input);
            valid = true;
        }
        catch (DateTimeParseException e)
        {
            //error
        }
        return valid;

    }

    public static boolean validateEmail(String mail)//check if email is in a valid pattern, would be mailable and if its already used
    {
        if(mail == null)
            return false;
        boolean valid = false;
        if (mail.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"))
        {
            try
            {
                InternetAddress internetAddress = new InternetAddress(mail);
                internetAddress.validate();
                if(MainBean.getEm().createNativeQuery("SELECT * FROM User WHERE EMail='" + mail + "'", User.class).getResultList().isEmpty())
                {
                    valid = true;
                }
                else
                {
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage("Diese E-Mail Adresse wurde bereits verwendet.", "Bitte versuchen Sie eine andere."));
                }
            }
            catch (AddressException ae)
            {
            }
        }
        return valid;
    }
    
    public static boolean validateNum(String num)//check if a string is a number(int)
    {
        if(num == null)
            return false;
        try
        {
            Integer.parseInt(num);
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }
}
