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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Henry
 */
@Entity
@Table(name = "GroupJoinRequest", schema = "gfosaward")
public class GroupJoinRequest implements Serializable
{
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")//Automatische generrierung vom Primärschlüssel
    @Column(name = "GroupJoinRequestID")
    private long gjrID;
     
    @OneToOne(targetEntity = WorkGroup.class)
    @JoinColumn(name = "WorkGroupID")
    private WorkGroup workGroup;
    
    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "UserID")
    private User user;

    public long getGjrID()
    {
        return gjrID;
    }

   
    public WorkGroup getWorkGroup()
    {
        return workGroup;
    }

    public void setWorkGroup(WorkGroup workGroup)
    {
        this.workGroup = workGroup;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
     
     
     
}
