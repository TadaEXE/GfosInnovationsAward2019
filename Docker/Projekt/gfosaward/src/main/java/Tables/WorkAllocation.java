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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Robin Sauerborn
 */
@Entity
@Table(name = "WorkAllocation", schema = "gfosaward")
public class WorkAllocation implements Serializable
{

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")//Automatische generrierung vom Primärschlüssel
    @Column(name = "WA_ID")
    private long waID;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne(targetEntity = Project.class)
    @JoinColumn(name = "ProjectID")
    private Project project;
    
    @ManyToOne(targetEntity = WorkGroup.class)
    @JoinColumn(name = "WorkGroupID")
    private WorkGroup workGroup;

    public long getWaID()
    {
        return waID;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Project getProject()
    {
        return project;
    }

    public void setProject(Project project)
    {
        this.project = project;
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
