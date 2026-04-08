package Tables;

import Util.Rank;
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
@Table(name = "User", schema = "gfosaward")
public class User implements Serializable
{

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")//Automatische generrierung vom Primärschlüssel
    @Column(name = "UserID")
    private long userID;                                                        //Primärschlüssel
    @Column(name = "Name")
    private String name;
    @Column(name = "LastName")
    private String lastName;
    @Column(name = "EMail")
    private String email;                                               
    @Column(name = "Birthday")
    private Date birthday;
    @Column(name = "RegestrationDay")
    private Date regestrationDay;
    @Column(name = "Password")
    private String password;
    @ManyToOne(targetEntity = Team.class)
    @JoinColumn(name = "TeamID")
    private Team team;
    @ManyToOne(targetEntity = WorkGroup.class)
    @JoinColumn(name = "WorkGroupID")
    private WorkGroup workGroup;
    @Column(name = "Verified")
    private boolean verified;
    @Column(name = "Rank")
    private Rank rank;
    @Column(name = "Score")
    private long score;

    public long getUserID()
    {
        return userID;
    }
    
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

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public Date getRegestrationDay()
    {
        return regestrationDay;
    }

    public void setRegestrationDay(Date regestrationDay)
    {
        this.regestrationDay = regestrationDay;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public WorkGroup getWorkGroup()
    {
        return workGroup;
    }

    public void setWorkGroup(WorkGroup workGroup)
    {
        this.workGroup = workGroup;
    }

    public boolean isVerified()
    {
        return verified;
    }

    public void setVerified(boolean verified)
    {
        this.verified = verified;
    }

    public Team getTeam()
    {
        return team;
    }

    public void setTeam(Team team)
    {
        this.team = team;
    }

    public Rank getRank()
    {
        return rank;
    }

    public void setRank(Rank rank)
    {
        this.rank = rank;
    }

    public long getScore()
    {
        return score;
    }

    public void setScore(long score)
    {
        this.score = score;
    }
    
    
    
}
