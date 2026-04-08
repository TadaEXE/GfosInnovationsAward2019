/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tables;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Henry
 */
@Entity
@Table(name = "SmtpConfig", schema = "gfosaward")
public class SmtpConfig implements Serializable
{
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")//Automatische generrierung vom Primärschlüssel
    @Column(name = "SmtpConfigID")
    private long smtpConfigID;  
    @Column(name = "SmtpHost")
    private String smtpHost;
    @Column(name = "SenderMail")
    private String senderMail;

    public long getSmtpConfigID()
    {
        return smtpConfigID;
    }

    
    public String getSmtpHost()
    {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost)
    {
        this.smtpHost = smtpHost;
    }

    public String getSenderMail()
    {
        return senderMail;
    }

    public void setSenderMail(String senderMail)
    {
        this.senderMail = senderMail;
    }
    
    
}
