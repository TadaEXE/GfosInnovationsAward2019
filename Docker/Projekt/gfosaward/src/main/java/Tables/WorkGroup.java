package Tables;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Robin Sauerborn
 */
@Entity
@Table(name = "WorkGroup", schema = "gfosaward")
public class WorkGroup implements Serializable
{

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")//Automatische generrierung vom Primärschlüssel
    @Column(name = "WorkGroupID")
    private long workGroupID;
    @Column(name = "Name")
    private String name;
    @OneToOne(targetEntity = SmtpConfig.class)
    @JoinColumn(name = "SmtpConfigID")
    private SmtpConfig smtpConfig;


    public long getWorkGroupID()
    {
        return workGroupID;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public SmtpConfig getSmtpConfig()
    {
        return smtpConfig;
    }

    public void setSmtpConfig(SmtpConfig smtpConfig)
    {
        this.smtpConfig = smtpConfig;
    }
    
    
}
