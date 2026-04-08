/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserBeanUtil;

import Tables.Project;
import Tables.Task;
import Tables.Team;
import Tables.User;
import java.io.Serializable;

/**
 *
 * @author Henry
 */
public class TreeTableObject implements Serializable, Comparable<TreeTableObject>
{
    private Object entity;//to send an object with this object for later use
    private String name;//the name wich will be displayed on the table
    private String classType;//the type of class from the entity to differentiate in xhtml

    public TreeTableObject(Object entity, String name, String classType)
    {
        this.entity = entity;
        this.name = name;
        this.classType = classType;
    }

    public Task getEntityAsTask()
    {
        return (Task)entity;
    }
    
    public Project getEntityAsProject()
    {
        return (Project)entity;
    }
    
    public User getEntityAsUser()
    {
        return (User)entity;
    }
    
    public Team getEntityAsTeam()
    {
        return (Team)entity;
    }
    
    @Override
    public int compareTo(TreeTableObject tto)
    {
        return this.getName().compareTo(tto.getName());
    }

    public Object getEntity()
    {
        return entity;
    }

    public void setEntity(Object entity)
    {
        this.entity = entity;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getClassType()
    {
        return classType;
    }

    public void setClassType(String classType)
    {
        this.classType = classType;
    }
    
    
    
}
