package Util;

import Tables.GroupJoinRequest;
import Tables.Task;
import Tables.User;

import Tables.Project;
import Tables.SmtpConfig;
import Tables.Team;

import Tables.WorkAllocation;
import Tables.WorkGroup;
import java.sql.Date;
import javax.persistence.EntityManager;

/**
 * 3.0
 *
 * @author Robin Sauerborn
 */

public class EntityFactory
{   //Custom Constructors are forbidden in Enitiys, so they are moved to this class and instantly saved to db
    //Every function creates an Object of needed kind and takes parameters to set everything up
    //Then it saves the Object to the DB and returns it
    
    public static GroupJoinRequest createGroupJoinRequestObject(WorkGroup workGroup, User user, EntityManager em)
    {
        GroupJoinRequest gjr = new GroupJoinRequest();
        gjr.setUser(user);
        gjr.setWorkGroup(workGroup);
        DBManager.createInDB(gjr, em);
        return gjr;
    }
    
    public static User createUserObject(String name, String lastName, 
          String email, Date birthday, Date regestrationDay, String password, EntityManager em)
    {
        User user = new User();
        
        user.setName(name);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setBirthday(birthday);
        user.setRegestrationDay(regestrationDay);
        user.setPassword(password);
        user.setWorkGroup(null);
        user.setTeam(null);
        user.setRank(Rank.Arbeiter);
        DBManager.createInDB(user, em);
        return user;
    }
    
     
    public static Project createProjectObject(String name, String description, 
            Date createdDate, Date deadlineDate, User creator, EntityManager em){
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setCreatedDate(createdDate);
        project.setDeadlineDate(deadlineDate);
        project.setCreator(creator.getUserID());
        
        DBManager.createInDB(project, em);
        return project;
    }

    public static Task createTaskObject(String name, String description, Date createdDate,
            Date deadlineDate, Project project, User user, User creator, long score, EntityManager em)
    {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setCreatedDate(createdDate);
        task.setDeadlineDate(deadlineDate);
        task.setProject(project);
        task.setScore(score);
        task.setTaskStatus(TaskStatus.In_Warteliste);
        task.setUser(user);
        task.setCreator(creator);
        DBManager.createInDB(task, em);
        return task;
    }
    
 
    
    public static WorkAllocation createWorkAllocationObject(WorkGroup workGroup, User user, Project project, EntityManager em){
        WorkAllocation workAllocation = new WorkAllocation();
    
        workAllocation.setProject(project);
        workAllocation.setUser(user);
        workAllocation.setWorkGroup(workGroup);
    
        DBManager.createInDB(workAllocation, em);
        return workAllocation;
    }
        
    public static Team createTeamObject(String name, WorkGroup workGroup, EntityManager em){
        Team team = new Team();
        team.setName(name);
        team.setWorkGroup(workGroup);
        
        DBManager.createInDB(team, em);
        return team;        
    }
    
    public static WorkGroup createWorkGroupObject(String name,EntityManager em){
        WorkGroup workGroup = new WorkGroup();
        workGroup.setName(name);
        workGroup.setSmtpConfig(createSmtpConfigObject("smtp", "", em));
        
        DBManager.createInDB(workGroup, em);
        return workGroup;
    }
    
    public static WorkGroup createWorkGroupObject(String name, SmtpConfig smtpConfig, EntityManager em){
        WorkGroup workGroup = new WorkGroup();
        workGroup.setName(name);
        workGroup.setSmtpConfig(smtpConfig);
        
        DBManager.createInDB(workGroup, em);
        return workGroup;
    }
    
    public static SmtpConfig createSmtpConfigObject(String smtpHost, String senderMail, EntityManager em)
    {
        SmtpConfig smtpConfig = new SmtpConfig();
        smtpConfig.setSmtpHost(smtpHost);
        smtpConfig.setSenderMail(senderMail);
        
        DBManager.createInDB(smtpConfig, em);
        return smtpConfig;
    }
}