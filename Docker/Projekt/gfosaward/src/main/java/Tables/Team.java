package Tables;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Robin Sauerborn
 */
@Entity
@Table(name = "Team", schema = "gfosaward")
public class Team implements Serializable
{

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")//Automatische generrierung vom Primärschlüssel
    @Column(name = "TeamID")
    private long teamID;
    @Column(name = "Name")
    private String name;
    @ManyToOne(targetEntity = WorkGroup.class)
    @JoinColumn(name = "WorkGroupID")
    private WorkGroup workGroup;

    public long getTeamID()
    {
        return teamID;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public WorkGroup getWorkGroup()
    {
        return workGroup;
    }

    public void setWorkGroup(WorkGroup workGroup)
    {
        this.workGroup = workGroup;
    }
    
    
}
