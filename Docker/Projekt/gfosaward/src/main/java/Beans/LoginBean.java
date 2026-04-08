package Beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import Tables.User;
import Util.Hashcode;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

/**
 *  Doku
 *      -loginUser()    :       Einloggen des Nutzers wenn er in der Datenbank exestiert
 */

@RequestScoped
@ManagedBean(name = "loginBean")
public class LoginBean
{
    private String email;
    private String password;

//<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
//</editor-fold>
    
    public String loginUser() {//check if user has an account an login him in if he does
        try {
            User user = (User) MainBean.getEm().createNativeQuery(//Sql requesting for the legitness of the given information
                    "SELECT * FROM User WHERE EMail='" + email +
                    "' AND password='" + Hashcode.encrypt(password) + "'", 
                    User.class).getSingleResult();
            FacesContext context = FacesContext.getCurrentInstance();
            context.getApplication().evaluateExpressionGet(context, "#{userBean}", UserBean.class).setCurrentUser(user);//login of user in backend
           
        } catch (Exception e) {
            PrimeFaces.current().executeScript("errorSquare('square logMailErr', true)");
            PrimeFaces.current().executeScript("errorSquare('square logPasswordErr', true)");
            return "";
        }
        return "Homescreen.xhtml?faces-redirect=true";//go to homepage
    }
}