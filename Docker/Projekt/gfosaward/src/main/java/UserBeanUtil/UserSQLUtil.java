/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserBeanUtil;

import Beans.MainBean;
import Tables.GroupJoinRequest;
import Tables.Project;
import Tables.Task;
import Tables.Team;
import Tables.User;
import Tables.WorkAllocation;
import Tables.WorkGroup;
import Util.DBManager;
import Util.Rank;
import Util.TaskStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Henry
 */
public class UserSQLUtil
{
    public static List<WorkAllocation> getWorkAllocationsOfProject(Project p)//Get all WorkAllocations that are connected to the given Project
    {
        List<WorkAllocation> wAs = new ArrayList<>();
        wAs.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM WorkAllocation WHERE ProjectID = '" + p.getProjectID() + "'", WorkAllocation.class).getResultList());
        return wAs;
    }
    public static List<WorkAllocation> getWorkAllocationsOfUser(User u)//Get all WorkAllocations that are connected to the given User
    {
        List<WorkAllocation> wAs = new ArrayList<>();
        wAs.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM WorkAllocation WHERE UserID = '" + u.getUserID() + "'", WorkAllocation.class).getResultList());
        return wAs;
    }
    
    public static List<Task> getTaskFromUser(User u)//Get all Tasks a User has
    {
        List<Task> tasks = new ArrayList<>();
        tasks.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM Task WHERE UserID = '" + u.getUserID() + "'", Task.class).getResultList());
        return tasks;
    }
    
    public static GroupJoinRequest getJoinRequestFromUser(User u)//Get the GroupJoinRequest from a User
    {
        List<GroupJoinRequest> temp = new ArrayList<>();
        temp.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM GroupJoinRequest WHERE UserID = '" + u.getUserID() + "'", GroupJoinRequest.class).getResultList());
        return temp.get(0);
    }
    public static Team getTeamByNameInGroup(WorkGroup wg, String name)//Get a Team from a WorkGroup by its name
    {
        List<Team> temp = new ArrayList<>();
        temp.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM Team WHERE WorkGroupID = '" + wg.getWorkGroupID() + "' AND Name = '" + name + "'", Team.class).getResultList());
        return temp.get(0);
    }
    
    public static boolean teamNameInGroup(WorkGroup wg, String name)//Check if a Team name is available in the given WrokGroup
    {
        List<Team> teams = new ArrayList<>();
        teams.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM Team WHERE WorkGroupID = '" + wg.getWorkGroupID() + "' AND Name = '" + name + "'", Team.class).getResultList());
        if(teams.isEmpty())
            return false;
        else
            return true;
    }
    
    public static boolean userIsJoining(User u)//Check if there is a GroupJoinRequest for the given User
    {

        List<GroupJoinRequest> temp = new ArrayList<>();
        temp.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM GroupJoinRequest WHERE UserID = '" + u.getUserID() + "'",GroupJoinRequest.class).getResultList());
        if(temp.isEmpty())
            return false;
        else
            return true;

    }
    
    public static List<User> getAllUsersInTeam(Team t)//Get all Useres that are in the given Team
    {
        List<User> users = new ArrayList<>();
        users.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM User WHERE TeamID = '" + t.getTeamID() + "'",User.class).getResultList());
        return users;
    }
    
    public static List<Team> getAllTeamsInGroup(WorkGroup wg)//Get all teams that are in the given WorkGroup
    {
        List<Team> teams = new ArrayList<>();
        teams.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM Team WHERE WorkGroupID = '" + wg.getWorkGroupID() + "'", Team.class).getResultList());
        return teams;
    }
    public static List<User> getAllJoinRequestsForGroup(WorkGroup wg)//Get all exsisting GroupJoinRequests for the given Group
    {
        List<User> users = new ArrayList<>();
        List<GroupJoinRequest> requests = new ArrayList<>();
        requests.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM GroupJoinRequest WHERE WorkGroupID = '" + wg.getWorkGroupID() + "'", GroupJoinRequest.class).getResultList());
        for(GroupJoinRequest gjr:requests)
        {
            users.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM User WHERE UserID = '" + gjr.getUser().getUserID() + "'", User.class).getResultList());
        }
        return users;
    }
    public static List<Task> getTasksFromUserInProject(User User, Project Project)//Get all tasks for a given User and a given Project
    {
        List<Task> ts = new ArrayList<>();
        ts.addAll(getTasksFromProject(Project));
        List<Task> filtered = new ArrayList<>();
        for(Task t:ts)
        {
            if(t.getUser().getUserID() == User.getUserID())
            {
                filtered.add(t);
            }
        }
        return filtered;
    }
    public static boolean userIsInProject(User User, Project Project)//Check if a given User is in a given Project
    {
        final long projectID = Project.getProjectID();
        List<Project> ps = new ArrayList<>();
        ps.addAll(getAllProjectsFromUser(User));
        Project result = ps.stream().filter(new Predicate<Project>()
        {
            @Override
            public boolean test(Project p)
            {
                return p.getProjectID() == projectID;
            }
        }).findFirst().orElse(null);
        return result != null;
    }
    public static Project getProjectInGroupWithName(String name, WorkGroup wg)//Get a Project by its name and the WorkGroup its in
    {
        final String predName = name;
        List<Project> ps = new ArrayList<>();
        ps.addAll(getAllProjectsFromWorkGroup(wg));
        Project result = ps.stream().filter(new Predicate<Project>()
        {
            @Override
            public boolean test(Project p)
            {
                return p.getName().equals(predName);
            }
        }).findFirst().get();
        return result;
    }
    public static boolean emailIsInGroup(String mail, WorkGroup wg)//Check if a given email exsists in a given WorkGroup
    {
        List<User> us = new ArrayList<>();
        us.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM User WHERE WorkGroupID = '" + wg.getWorkGroupID() + "' AND EMail = '" + mail + "'", User.class).getResultList());
        if(!us.isEmpty())
            return true;
        else
            return false;
    }
    public static List<User> getAllUsersFromGroup(WorkGroup wg)//Get all Users that are in a given WorkGroup
    {
        List<User> us = new ArrayList<>();
        us.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM User WHERE WorkGroupID = '" + wg.getWorkGroupID() + "'", User.class).getResultList());
        return us;
    }
    
    public static Task getTaskWithID(long id)//Find User in DB that has a given id
    {
        List<Task> ts = new ArrayList<>();
        ts.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM Task WHERE TaskID = '" + id + "'", Task.class).getResultList());
        return ts.get(0);
    }

    public static User getUserWithMail(String mail)//Find User in DB that has a given mail adress
    {
        List<User> us = new ArrayList<>();
        us.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM User WHERE EMail = '" + mail + "'", User.class).getResultList());
        return us.get(0);
    }

    public static Project getProjectWithID(long id)//Find Project in DB that has a given id
    {
        List<Project> ps = new ArrayList<>();
        ps.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM Project WHERE ProjectID = '" + id + "'", Project.class).getResultList());
        return ps.get(0);
    }

    public static List<User> getAllUserInGroupWithRank(WorkGroup workGroup, Util.Rank rank)//Find all users in a group that have a certaint rank
    {
        List<User> users = new ArrayList<>();
        users.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM User WHERE WorkGroupID = '" + workGroup.getWorkGroupID() + "' AND Rank = '" + rank + "'", User.class).getResultList());
        return users;
    }

    public static WorkGroup getWorkGroupWithName(String name)//Find a WorkGroup that has a certaint name
    {
        List<WorkGroup> wgs = new ArrayList<>();
        wgs.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM WorkGroup WHERE Name = '" + name + "'", WorkGroup.class).getResultList());
        return wgs.get(0);
    }

    public static boolean workGroupNameTaken(String name)//determin if a WorkGroup name is already taken
    {
        if (MainBean.getEm().createNativeQuery("SELECT * FROM WorkGroup WHERE Name = '" + name + "'", WorkGroup.class).getResultList().isEmpty())
        {
            return false;
        }
        return true;
    }

    public static List<Task> getAllTaskFromUser(User User)//get all saved tasks from one User
    {
        List<Task> result = new ArrayList<>();
        try
        {
            result.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM Task WHERE UserID ='" + User.getUserID() + "'", Task.class).getResultList());
        }
        catch (NullPointerException npe)
        {
        }
        return result;
    }

    public static List<Task> getTaskWithName(String name)//get all tasks that have a certaint name
    {
        List<Task> result = new ArrayList<>();
        try
        {
            result.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM Task WHERE Name = '" + name + "'", Task.class).getResultList());
        }
        catch (NullPointerException npe)
        {
        }
        return result;
    }

    public static List<Task> getTaskWithStatus(TaskStatus status)//get all tasks that have a certaint status
    {
        List<Task> result = new ArrayList<>();
        try
        {
            result.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM Task WHERE Status = '" + status + "'", Task.class).getResultList());
        }
        catch (NullPointerException npe)
        {
        }
        return result;
    }
    
    public static boolean projectNameIsInGroup(String pName, WorkGroup wg)//Check if a given Project name is in a given WorkGroup
    {
        List<WorkAllocation> wAs = MainBean.getEm().createNativeQuery("SELECT * FROM WorkAllocation WHERE WorkGroupID = '" + wg.getWorkGroupID() + "'", WorkAllocation.class).getResultList();
        List<Project> ps = new ArrayList();
        for(int i = 0; i < wAs.size(); i++)
        {
            WorkAllocation wa = wAs.get(i);
            ps.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM Project WHERE ProjectID = '" + wa.getProject().getProjectID() + "'", Project.class).getResultList());
            if(ps.get(i).getName().equals(pName))
            {
                return true;
            }
        }
        return false;
    }
    
    public static List<Project> getAllProjectsFromWorkGroup(WorkGroup wg)//Get all Project that are in a given WorkGroup
    {
        List<WorkAllocation> wAs = new ArrayList<>();
        wAs.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM WorkAllocation WHERE WorkGroupID = '" + wg.getWorkGroupID() + "'", WorkAllocation.class).getResultList());
       
        //filtering of duplicates in projectIDs
        List<Long> exsisting = new ArrayList<>();
        for(int x = 0; x < wAs.size(); x++)
        {
            if(exsisting.contains(wAs.get(x).getProject().getProjectID()))
            {
                wAs.remove(wAs.get(x));
                x--;
            }
            else
            {
                exsisting.add(wAs.get(x).getProject().getProjectID());
            }
        }
        
        List<Project> ps = new ArrayList();
        for (WorkAllocation wa : wAs)
        {
            ps.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM Project WHERE ProjectID = '" + wa.getProject().getProjectID() + "'", Project.class).getResultList());
        }
        
        return ps;
    }

    public static List<Task> getTasksFromProject(Project Project)//get all tasks that are in a given Project
    {
        List<Task> result = new ArrayList<>();
        try
        {
            result.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM Task WHERE ProjectID = '" + Project.getProjectID() + "'", Task.class).getResultList());
        }
        catch (NullPointerException npe)
        {
        }
        return result;
    }

    public static List<Project> getAllProjectsFromUser(User User)//get all projects a User is part of
    {
        List<Project> result = new ArrayList<>();
        try
        {
            List<WorkAllocation> temp = new ArrayList<>();
            temp.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM WorkAllocation WHERE UserID = '" + User.getUserID() + "'", WorkAllocation.class).getResultList());
            for (WorkAllocation wa : temp)
            {
                result.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM Project WHERE ProjectID = '" + wa.getProject().getProjectID() + "'", Project.class).getResultList());
            }
        }
        catch (NullPointerException npe)
        {
        }
        return result;
    }

    public static void deleteGroupInDB(WorkGroup wg)//delete every dependency on a group and the group it self
    {
        List<WorkAllocation> wAs = MainBean.getEm().createNativeQuery("SELECT * FROM WorkAllocation WHERE WorkGroupID = '" + wg.getWorkGroupID() + "'", WorkAllocation.class).getResultList();
        List<Project> ps = new ArrayList();
        for (WorkAllocation wa : wAs)
        {
            ps.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM Project WHERE ProjectID = '" + wa.getProject().getProjectID() + "'", Project.class).getResultList());
        }
        List<Task> ts = new ArrayList();
        for (Project p : ps)
        {
            ts.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM Task WHERE ProjectID = '" + p.getProjectID() + "'", Task.class).getResultList());
        }
        List<Team> teams = MainBean.getEm().createNativeQuery("SELECT * FROM Team WHERE WorkGroupID = '" + wg.getWorkGroupID() + "'", Team.class).getResultList();

        List<User> groupUsers = MainBean.getEm().createNativeQuery("SELECT * FROM User WHERE WorkGroupID = '" + wg.getWorkGroupID() + "'", User.class).getResultList();

        for (User u : groupUsers)
        {
            u.setTeam(null);
            u.setWorkGroup(null);
            u.setRank(Rank.Arbeiter);
            u.setScore(0);
            DBManager.saveInDB(u, MainBean.getEm());
        }

        for (Task t : ts)
        {
            DBManager.deleteFromDB(t, MainBean.getEm());
        }
        for (WorkAllocation wa : wAs)
        {
            DBManager.deleteFromDB(wa, MainBean.getEm());
        }

        for (Project p : ps)
        {
            DBManager.deleteFromDB(p, MainBean.getEm());
        }

        for (Team t : teams)
        {
            DBManager.deleteFromDB(t, MainBean.getEm());
        }

        DBManager.deleteFromDB(wg, MainBean.getEm());
        DBManager.deleteFromDB(wg.getSmtpConfig(), MainBean.getEm());

    }

    public static List<WorkGroup> getAllWorkGroups()//get all exsiting groups
    {
        List<WorkGroup> result = new ArrayList<>();
        result.addAll(MainBean.getEm().createNativeQuery("SELECT * FROM WorkGroup", WorkGroup.class).getResultList());
        return result;
    }
}
