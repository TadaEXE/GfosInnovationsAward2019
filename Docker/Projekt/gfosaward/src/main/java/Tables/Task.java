/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tables;

//Sollte Verwaltung nicht in das Tabellen package? Es ist eine SQL Entity Tabelle. Was hat eine Aufgabe mit der Verwaltung zu tun?
import Util.TaskStatus;
import java.io.Serializable;
import java.sql.Date;
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

@Entity
@Table(name = "Task", schema = "gfosaward")
public class Task implements Serializable
{

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")//Automatische generrierung vom Primärschlüssel
    @Column(name = "TaskID")
    private long taskID;
    @Column(name = "Name")
    private String name;
    @Column(name = "Description")
    private String description;
    @Column(name = "Score")
    private long score;
    @Column(name = "CreatedDate")
    private Date createdDate;
    @Column(name = "DeadlineDate")
    private Date deadlineDate;
    @Column(name = "Status")
    private TaskStatus taskStatus;
    @ManyToOne(targetEntity = Project.class)
    @JoinColumn(name = "ProjectID")
    private Project project;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "UserID")
    private User user;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "Creator")
    private User creator;    
    
    
    public User getCreator()
    {
        return creator;
    }

    public void setCreator(User creator)
    {
        this.creator = creator;
    }
    
    

    public Project getProject()
    {
        return project;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    public long getTaskID()
    {
        return taskID;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public long getScore()
    {
        return score;
    }

    public void setScore(long score)
    {
        this.score = score;
    }
    
    

    public Date getDeadlineDate()
    {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate)
    {
        this.deadlineDate = deadlineDate;
    }

    public TaskStatus getTaskStatus()
    {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus)
    {
        this.taskStatus = taskStatus;
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
