/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Tables.Project;
import Tables.Task;
import Tables.Team;
import Tables.User;
import Tables.WorkGroup;
import UserBeanUtil.TreeTableObject;
import UserBeanUtil.UserSQLUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Henry
 */
@SessionScoped
@ManagedBean(name = "userBean")
public class UserBean {

    //<Für xhtml>
    private String curTab = "Taskscreen";
    //</Für xhtml>

    private TreeTableObject selectedTreeObj;
    private static User currentUser = null;
    private List<User> userByScore = new ArrayList<>();
    private ScheduleModel taskModel;
    private Task selectedEventTask;

    public Project getProjectRef(long id) {
        return UserSQLUtil.getProjectWithID(id);
    }

    public boolean userHasMinRank(int minRank) {
        if (currentUser != null && currentUser.getRank() != null) {
            return currentUser.getRank().ordinal() >= minRank;
        } else {
            return false;
        }
    }

    public void loadScoreboard() {
        if (userByScore != null) {
            userByScore = new ArrayList<>();
        }

        userByScore = UserSQLUtil.getAllUsersFromGroup(currentUser.getWorkGroup());
        final List<User> temp = new ArrayList<>();
        userByScore.stream().sorted(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return Long.compare(o1.getScore(), o2.getScore()) * -1;
            }
        }).forEach(new Consumer<User>() {
            @Override
            public void accept(User t) {
                temp.add(t);
            }
        });
        userByScore.clear();
        userByScore.addAll(temp);

    }

    public void showAllGroups() {
        List<WorkGroup> w = UserSQLUtil.getAllWorkGroups();
        PrimeFaces.current().executeScript("updateLists()");

        if (w.isEmpty()) {
            PrimeFaces.current().executeScript("addGroupToList('(Keine vorhanden)')");
        } else {
            for (WorkGroup wg : w) {
                PrimeFaces.current().executeScript("addGroupToList('" + wg.getName() + "')");
            }
        }
    }

    public void projectScreenSetup() {
        if (currentUser.getWorkGroup() != null) {
            PrimeFaces.current().executeScript("updateLists()");
            showAllProjectsOfGroup();
            showAllUsersOfGroup();
        }
    }

    public void groupScreenSetup() {
        if (currentUser.getWorkGroup() != null) {
            PrimeFaces.current().executeScript("updateLists()");
            List<Team> t = UserSQLUtil.getAllTeamsInGroup(currentUser.getWorkGroup());
            if (t.isEmpty()) {
                PrimeFaces.current().executeScript("addTeamToList('(Keine vorhanden)')");
            } else {
                for (Team tm : t) {
                    PrimeFaces.current().executeScript("addTeamToList('" + tm.getName() + "')");
                }
            }
            
            if(!userHasMinRank(2))
            {
                PrimeFaces.current().executeScript("inputReadOnly(true)");
            }
            else
            {
                PrimeFaces.current().executeScript("inputReadOnly(false)");
            }
        }
    }

    public void showAllProjectsOfGroup() {
        List<Project> p = UserSQLUtil.getAllProjectsFromWorkGroup(currentUser.getWorkGroup());

        if (p.isEmpty()) {
            PrimeFaces.current().executeScript("addProjectToList('(Keine vorhanden)')");
        } else {
            for (Project pj : p) {
                PrimeFaces.current().executeScript("addProjectToList('" + pj.getName() + "')");
            }
        }
    }

    public void onTaskEventSelect(SelectEvent event) {
        if (event != null) {
            ScheduleEvent tempEvent = ((ScheduleEvent) event.getObject());
            if (tempEvent != null) {
                selectedEventTask = ((Task) tempEvent.getData());
            }
        }
    }

    public void showAllUsersOfGroup() {
        List<User> u = UserSQLUtil.getAllUsersFromGroup(currentUser.getWorkGroup());

        if (u.isEmpty()) {
            PrimeFaces.current().executeScript("addUserToList('(Keine vorhanden)')");
        } else {
            for (User user : u) {
                PrimeFaces.current().executeScript("addUserToList('" + user.getEmail() + "')");
            }
        }
    }

    public void loadTaskScheduel() {
        taskModel = new DefaultScheduleModel();
        List<Task> usersTasks = UserSQLUtil.getAllTaskFromUser(currentUser);
        for (Task t : usersTasks) {
            taskModel.addEvent(new DefaultScheduleEvent(t.getName(), t.getCreatedDate(), t.getDeadlineDate(), t));
        }

    }

    public String currentUserPosString() {
        String result = "Keine Platzierung";
        if (currentUser.getWorkGroup() != null) {
            List<User> allUsers = UserSQLUtil.getAllUsersFromGroup(currentUser.getWorkGroup());
            final List<User> temp = new ArrayList<>();
            allUsers.stream().sorted(new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return Long.compare(o1.getScore(), o2.getScore()) * -1;
                }
            }).forEach(new Consumer<User>() {
                @Override
                public void accept(User t) {
                    temp.add(t);
                }
            });
            allUsers.clear();
            allUsers.addAll(temp);
            int pos = allUsers.indexOf(currentUser);
            result = (pos + 1) + ". Platz";
        }
        return result;
    }

    public void logout() {
        currentUser = null;
        try {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().getExternalContext().redirect("./../../"); //wildfly
        } catch (IOException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TreeNode genRequestedUsersInGroup() {
        TreeNode header = new DefaultTreeNode(new TreeTableObject(-1, "Gruppe", "root"), null);
        if (currentUser.getWorkGroup() != null) {
            List<User> users = UserSQLUtil.getAllJoinRequestsForGroup(currentUser.getWorkGroup());
            
            for (User u : users) {
                TreeNode taskNode = new DefaultTreeNode(new TreeTableObject(u, u.getEmail(), "Nutzer"), header);
            }
        }
        return header;
    }
    

    public TreeNode genUsersInGroup() {
        TreeNode header = new DefaultTreeNode(new TreeTableObject(-1, "Gruppe", "root"), null);
        if (currentUser.getWorkGroup() != null) {
            List<Team> teams = UserSQLUtil.getAllTeamsInGroup(currentUser.getWorkGroup());

            for (Team t : teams) {
                TreeNode teamTop = new DefaultTreeNode(new TreeTableObject(t, t.getName(), "Team"), header);
                teamTop.setExpanded(true); // ACHTUNG: Primefaces Geist möchte das
                List<User> users = UserSQLUtil.getAllUsersInTeam(t);
                for (User u : users) {
                    TreeNode taskNode = new DefaultTreeNode(new TreeTableObject(u, u.getEmail(), "Nutzer"), teamTop);
                }
            }

            TreeNode noTeamTop = new DefaultTreeNode(new TreeTableObject(null, "Keine Vorhanden", "Nicht zugewiesen"), header);
            noTeamTop.setExpanded(true);
            List<User> usersWithoutTeam = UserSQLUtil.getAllUsersFromGroup(currentUser.getWorkGroup());
            for (User u : usersWithoutTeam) {
                if (u.getTeam() == null) {
                    TreeNode taskNode = new DefaultTreeNode(new TreeTableObject(u, u.getEmail(), "Nutzer"), noTeamTop);
                }
            }
        }

        return header;
    }

    public TreeNode genTaskTable() {
        TreeNode header = new DefaultTreeNode(new TreeTableObject(-1, "Projekte", "root"), null);
        if (currentUser.getWorkGroup() != null) {
            List<Project> projects = UserSQLUtil.getAllProjectsFromUser(currentUser);

            for (Project p : projects) {
                TreeNode projectTop = new DefaultTreeNode(new TreeTableObject(p, p.getName(), "Projekt"), header);
                projectTop.setExpanded(true); // ACHTUNG: Primefaces Geist möchte das
                List<Task> tasks = UserSQLUtil.getTasksFromUserInProject(currentUser, p);
                for (Task t : tasks) {
                    TreeNode taskNode = new DefaultTreeNode(new TreeTableObject(t, t.getName(), "Aufgabe"), projectTop);
                }
            }
        }

        return header;
    }

    public TreeNode genProjectTable() {
        TreeNode header = new DefaultTreeNode(new TreeTableObject(-1, "Projekte", "root"), null);
        if (currentUser.getWorkGroup() != null) {
            List<Project> projects = UserSQLUtil.getAllProjectsFromWorkGroup(currentUser.getWorkGroup());

            for (Project p : projects) {
                TreeNode projectTop = new DefaultTreeNode(new TreeTableObject(p, p.getName(), "Projekt"), header);
                projectTop.setExpanded(true); // ACHTUNG: Primefaces Geist möchte das
                List<Task> tasks = UserSQLUtil.getTasksFromProject(p);
                for (Task t : tasks) {
                    TreeNode taskNode = new DefaultTreeNode(new TreeTableObject(t, t.getName(), "Aufgabe"), projectTop);
                }
            }
        }

        return header;
    }

    //<editor-fold defaultstate="collapsed" desc="Getter/Setter">
    public Task getSelectedEventTask() {
        return selectedEventTask;
    }

    public void setSelectedEventTask(Task selectedEventTask) {
        this.selectedEventTask = selectedEventTask;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        UserBean.currentUser = currentUser;
    }

    public String getCurTab() {
        return curTab;
    }

    public void setCurTab(String curTab) {
        if (currentUser.getWorkGroup() == null) {
            if (UserSQLUtil.userIsJoining(currentUser)) {
                this.curTab = "Joiningscreen";
                return;
            }
            this.curTab = "Welcomescreen";
            return;
        }

        if (curTab.equals("Taskscreen")) {

        }
        this.curTab = curTab;
    }

    public TreeTableObject getSelectedTreeObj() {
        return selectedTreeObj;
    }

    public void setSelectedTreeObj(TreeTableObject selectedTreeObj) {
        this.selectedTreeObj = selectedTreeObj;
    }

    public List<User> getUserByScore() {
        return userByScore;
    }

    public void setUserByScore(List<User> userByScore) {
        this.userByScore = userByScore;
    }

    public ScheduleModel getTaskModel() {
        return taskModel;
    }

    public void setTaskModel(ScheduleModel taskModel) {
        this.taskModel = taskModel;
    }

    //</editor-fold>
}
