package Tables;

import java.io.Serializable;
import java.sql.Date;
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
 * @author Robin Sauerborn
 */
@Entity
@Table(name = "Project", schema = "gfosaward")
public class Project implements Serializable
{

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")//Automatische generrierung vom Primärschlüssel
    @Column(name = "ProjectID")
    private long projectID;
    @Column(name = "Name")
    private String name;
    @Column(name = "Description")
    private String description;
    @Column(name = "CreatedDate")
    private Date createdDate;
    @Column(name = "DeadlineDate")
    private Date deadlineDate;
    @Column(name = "CreatorID")
    private long creatorID;

    public long getProjectID()
    {
        return projectID;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public Date getDeadlineDate()
    {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate)
    {
        this.deadlineDate = deadlineDate;
    }

    public long getCreator()
    {
        return creatorID;
    }

    public void setCreator(long creatorID)
    {
        this.creatorID = creatorID;
    }
}
