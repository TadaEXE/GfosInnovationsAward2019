/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Tables.GroupJoinRequest;
import Tables.Project;
import Tables.SmtpConfig;
import Tables.Task;
import Tables.Team;
import Tables.User;
import Tables.WorkAllocation;
import Tables.WorkGroup;
import UserBeanUtil.MailUtil;
import UserBeanUtil.UserSQLUtil;
import Util.DBManager;
import Util.DateConverter;
import Util.EntityFactory;
import Util.Hashcode;
import Util.InputValidation;
import Util.Rank;
import Util.TaskStatus;
import java.io.IOException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Henry
 */
@SessionScoped
@ManagedBean(name = "userDataBean")
public class UserDataBean
{

    private User currentUser = null;

    private String change_name;
    private String change_lastname;
    private String change_email;
    private String change_password;
    private String change_verifyPassword;
    private String pw_password;
    private String pw_newPassword;
    private String pw_newVerifyPassword;
    private String create_groupname;

    private String change_groupname;

    //Group join
    private String join_GroupName;

    //Task creation
    private String createT_Name;
    private String createT_Description;
    private Date createT_DeadlineDate;
    private String createT_ProjectName;
    private String createT_Score;
    private String createT_Usermail;

    //Project creation
    private String createP_Name;
    private String createP_Description;
    private Date createP_DeadlineDate;

    //Team creation
    private String createTeam_Name;

    //Change team at User
    private String changeTeam_Name;
    private String changeTeam_Rank;

    //SMTP settings
    private String smtp_Host;
    private String smtp_Mail;

    //Change Project
    private java.util.Date changeP_DeadlineDate;

    //Change Task
    private java.util.Date changeT_DeadlineDate;
    private String changeT_ProjectName;
    private String changeT_UserMail;
    private String changeT_Status;
    private String changeT_score;

    //Change own Task
    private String changeTOwn_Status;
    
    public void deleteGroup()
    {
        UserSQLUtil.deleteGroupInDB(currentUser.getWorkGroup());
        FacesContext context = FacesContext.getCurrentInstance();
        context.getApplication().evaluateExpressionGet(context, "#{userBean}", UserBean.class).setCurTab("Homescreen");
    }

    public void cancelGroupJoin()
    {
        System.out.println("heeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        GroupJoinRequest gjr = UserSQLUtil.getJoinRequestFromUser(currentUser);
        DBManager.deleteFromDB(gjr, MainBean.getEm());
    }

    public void deleteProject(Project p)
    {
        List<WorkAllocation> projectAllos = UserSQLUtil.getWorkAllocationsOfProject(p);
        List<Task> projectTasks = UserSQLUtil.getTasksFromProject(p);
        for (WorkAllocation wa : projectAllos)
        {
            DBManager.deleteFromDB(wa, MainBean.getEm());
        }
        for (Task t : projectTasks)
        {
            DBManager.deleteFromDB(t, MainBean.getEm());
        }
        DBManager.deleteFromDB(p, MainBean.getEm());
    }

    public void acceptUser(User u)
    {
        u.setWorkGroup(currentUser.getWorkGroup());
        GroupJoinRequest gjr = UserSQLUtil.getJoinRequestFromUser(u);
        DBManager.deleteFromDB(gjr, MainBean.getEm());
        DBManager.saveInDB(u, MainBean.getEm());
    }

    public void denieUser(User u)
    {
        GroupJoinRequest gjr = UserSQLUtil.getJoinRequestFromUser(u);
        DBManager.deleteFromDB(gjr, MainBean.getEm());
    }

    public void kickUser(User u)
    {
        if (!u.getEmail().equals(currentUser.getEmail()))
        {
            u.setWorkGroup(null);
            u.setScore(0);
            u.setTeam(null);
            List<WorkAllocation> was = UserSQLUtil.getWorkAllocationsOfUser(u);
            for (WorkAllocation wa : was)
            {
                DBManager.deleteFromDB(wa, MainBean.getEm());
            }
            List<Task> userTasks = UserSQLUtil.getTaskFromUser(u);
            for (Task t : userTasks)
            {
                DBManager.deleteFromDB(t, MainBean.getEm());
            }
            u.setRank(Rank.Arbeiter);
            DBManager.saveInDB(u, MainBean.getEm());
        }
        else
        {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Aktion nicht möglich", "Sie können sich nicht selber aus der Gruppe werfen.\nBitte verlassen Sie die Gruppe in Ihren Profileinstellungen."));
        }
    }

    public void changeSMTP()
    {

        if (validateSmtp())
        {
            currentUser.getWorkGroup().getSmtpConfig().setSenderMail(smtp_Mail);
            currentUser.getWorkGroup().getSmtpConfig().setSmtpHost(smtp_Host);
            DBManager.saveInDB(currentUser.getWorkGroup().getSmtpConfig(), MainBean.getEm());
            PrimeFaces.current().executeScript("closeOverlaySmtp()");
        }

    }

    private boolean validateSmtp()
    {
        List<String> falseSquares = new ArrayList<>();
        if (smtp_Host.isEmpty())
        {
            falseSquares.add("square creSHostErr");
        }
        if (smtp_Mail.isEmpty())
        {
            falseSquares.add("square creSMailErr");
        }

        if (!falseSquares.isEmpty())
        {
            for (String s : falseSquares)
            {
                PrimeFaces.current().executeScript("errorSquare('" + s + "', true)");
            }
            return false;
        }

        return true;
    }

    public void createTeam()
    {
        if (!createTeam_Name.isEmpty())
        {
            if (!UserSQLUtil.teamNameInGroup(currentUser.getWorkGroup(), createTeam_Name))
            {
                //growl name vergeben
                EntityFactory.createTeamObject(createTeam_Name, currentUser.getWorkGroup(), MainBean.getEm());
                PrimeFaces.current().executeScript("closeOverlayMemberJS()");
            }
        }
        else
        {
            PrimeFaces.current().executeScript("errorSquare('square creTeamNameErr', true)");
        }
    }

    public void clearProjectCreateSquares()
    {
        List<String> squares = new ArrayList<>();

        squares.add("square crePNameErr");
        squares.add("square crePDescriptionErr");
        squares.add("square crePDeadlineErr");
        for (String s : squares)
        {
            PrimeFaces.current().executeScript("errorSquare('" + s + "', false)");
        }
    }

    public void closeProjectCreate()
    {
        PrimeFaces.current().executeScript("setupAfterProject()");
        PrimeFaces.current().executeScript("PF('projectCreate').hide()");
        clearProjectCreateSquares();
        createP_Name = "";
        createP_Description = "";
        createP_DeadlineDate = null;
    }

    public void clearTaskCreateSquares()
    {
        List<String> squares = new ArrayList<>();
        squares.add("square creTNameErr");
        squares.add("square creTDescriptionErr");
        squares.add("square creTScoreErr");
        squares.add("square creTDeadlineErr");
        for (String s : squares)
        {
            PrimeFaces.current().executeScript("errorSquare('" + s + "', false)");
        }
    }

    public void closeTaskCreate()
    {
        PrimeFaces.current().executeScript("PF('taskCreate').hide()");
        clearTaskCreateSquares();
        createT_Name = "";
        createT_Description = "";
        createT_DeadlineDate = null;
        createT_ProjectName = "";
        createT_Score = "";
        createT_Usermail = "";
    }

    public void createTask()
    {
        clearTaskCreateSquares();
        if (validateTask())
        {
            User u = UserSQLUtil.getUserWithMail(createT_Usermail);
            Project p = UserSQLUtil.getProjectInGroupWithName(createT_ProjectName, currentUser.getWorkGroup());
            Task task = EntityFactory.createTaskObject(createT_Name, createT_Description, java.sql.Date.valueOf(LocalDate.now()), DateConverter.utilDateToSqlDate(createT_DeadlineDate), p, u, currentUser, Long.valueOf(createT_Score), MainBean.getEm());
            if (!UserSQLUtil.userIsInProject(u, p))
            {
                WorkAllocation wa = EntityFactory.createWorkAllocationObject(currentUser.getWorkGroup(), u, p, MainBean.getEm());
                DBManager.createInDB(wa, MainBean.getEm());
            }
            if (MailUtil.sendTaskAssignmentMail(task, u, currentUser, currentUser.getWorkGroup()))
            {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Erfolgreich", "Aufgabe erstellt."));
            }
            PrimeFaces.current().executeScript("taskResetter()");
        }
        else
        {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Fehlgeschlagen", "Aufgabe konnte nicht erstellt werden.\nBitte überprüfen Sie ihre Eingabe."));
        }
    }

    public void createTaskReq()
    {
        PrimeFaces.current().executeScript("createTaskJS()");
    }

    public void changeTeamReq() //unnötig
    {
        PrimeFaces.current().executeScript("changeTeamJS()");
    }

    public void changeTaskReq()
    {
        PrimeFaces.current().executeScript("changeTaskJS()");
    }

    public boolean validateTask()
    {
        List<String> falseSquares = new ArrayList<>();
        if (createT_Name.isEmpty())
        {
            falseSquares.add("square creTNameErr");
        }
        if (createT_Description.isEmpty())
        {
            falseSquares.add("square creTDescriptionErr");
        }
        if (!InputValidation.validateNum(createT_Score) || createT_Score.isEmpty())
        {
            falseSquares.add("square creTScoreErr");
        }
        if (!UserSQLUtil.projectNameIsInGroup(createT_ProjectName, currentUser.getWorkGroup()) || createT_ProjectName.isEmpty())
        {
            falseSquares.add("square creTprojectErr");
        }
        if (!UserSQLUtil.emailIsInGroup(createT_Usermail, currentUser.getWorkGroup()) || createT_Usermail.isEmpty())
        {
            falseSquares.add("square creTuserErr");
        }
        if (createT_DeadlineDate == null || createT_DeadlineDate.before(DateConverter.utilDateToSqlDate(java.sql.Date.valueOf(LocalDate.now()))))
        {
            falseSquares.add("square creTDeadlineErr");
        }

        if (!falseSquares.isEmpty())
        {
            for (String s : falseSquares)
            {
                PrimeFaces.current().executeScript("errorSquare('" + s + "', true)");
            }
            return false;
        }

        return true;
    }

    public void changeOwnTaskStatus(Task t)
    {
        TaskStatus ts = null;
        try
        {
            ts = TaskStatus.valueOf(changeTOwn_Status);
        }
        catch (Exception ex)
        {
            PrimeFaces.current().executeScript("errorSquare('square changeMyTaskErr', true)");
            return;
        }

        if (ts != null)
        {
            t.setTaskStatus(ts);
            DBManager.saveInDB(t, MainBean.getEm());
            PrimeFaces.current().executeScript("PF('taskEventDialogWV').hide()");
            MailUtil.sendTaskStatusChangeMail(currentUser.getWorkGroup(), t);
        }

    }

    public void changeTask(Task t)
    {
        if (validateChangeTask(t))
        {
            t.setDeadlineDate(DateConverter.utilDateToSqlDate(changeT_DeadlineDate));
            TaskStatus ts = TaskStatus.valueOf(changeT_Status);
            if(t.getTaskStatus() != ts)
            {
                t.setTaskStatus(ts);
                MailUtil.sendTaskStatusChangeMail(currentUser.getWorkGroup(), t);
            }
            Project p = UserSQLUtil.getProjectInGroupWithName(changeT_ProjectName, currentUser.getWorkGroup());
            User cur = UserSQLUtil.getUserWithMail(changeT_UserMail);
            t.setScore(Long.parseLong(changeT_score));            
            t.setUser(cur);
            t.setProject(p);
            DBManager.saveInDB(t, MainBean.getEm());
            PrimeFaces.current().executeScript("closeOverlay()");
        }
    }

    public void closeOverlayReq()
    {
        PrimeFaces.current().executeScript("PF('projectTreeDialog').hide()");
    }

    public void closeOverlayMemberReq()
    {
        PrimeFaces.current().executeScript("PF('MemberRequestTreeDialog').hide()");
        PrimeFaces.current().executeScript("PF('teamCreate').hide()");
    }

    private boolean validateChangeTask(Task t)
    {
        List<String> falseSquares = new ArrayList<>();
        if (t.getName().isEmpty())
        {
            falseSquares.add("square changeTNameErr");
        }
        if (t.getDescription().isEmpty())
        {
            falseSquares.add("square changeTDescriptionErr");
        }
        if (changeT_score.isEmpty() || !InputValidation.validateNum(changeT_score))
        {
            falseSquares.add("square changeTScoreErr");
        }
        if (!UserSQLUtil.projectNameIsInGroup(changeT_ProjectName, currentUser.getWorkGroup()))
        {
            falseSquares.add("square changeTprojectErr");
        }
        if (!UserSQLUtil.emailIsInGroup(changeT_UserMail, currentUser.getWorkGroup()))
        {
            falseSquares.add("square changeTuserErr");
        }
        if (changeT_DeadlineDate == null || changeT_DeadlineDate.before(DateConverter.utilDateToSqlDate(java.sql.Date.valueOf(LocalDate.now()))))
        {
            falseSquares.add("square changeTDeadlineErr");
        }

        if (!falseSquares.isEmpty())
        {
            for (String s : falseSquares)
            {
                PrimeFaces.current().executeScript("errorSquare('" + s + "', true)");
            }
            return false;
        }

        return true;
    }

    public void changeTeam(Team t)
    {
        DBManager.saveInDB(t, MainBean.getEm());
        PrimeFaces.current().executeScript("closeOverlay()");
    }

    public void changeProject(Project p)
    {
        if (validateChangeProject(p))
        {
            p.setDeadlineDate(DateConverter.utilDateToSqlDate(changeP_DeadlineDate));
            DBManager.saveInDB(p, MainBean.getEm());
            PrimeFaces.current().executeScript("closeOverlay()");
        }
    }

    private boolean validateChangeProject(Project p)
    {
        List<String> falseSquares = new ArrayList<>();
        if (p.getName().isEmpty())
        {
            falseSquares.add("square changePNameTDErr");
        }
        if (p.getDescription().isEmpty())
        {
            falseSquares.add("square changePDescriptionTDErr");
        }
        if (changeP_DeadlineDate == null || changeP_DeadlineDate.before(DateConverter.utilDateToSqlDate(java.sql.Date.valueOf(LocalDate.now()))))
        {
            falseSquares.add("square changePDeadlineTDErr");
        }

        if (!falseSquares.isEmpty())
        {
            for (String s : falseSquares)
            {
                PrimeFaces.current().executeScript("errorSquare('" + s + "', true)");
            }
            return false;
        }

        return true;
    }

    public void changeUser(User u)
    {
        Rank r;
        try
        {
         r = Rank.valueOf(changeTeam_Rank);
        }catch(Exception ex)
        {
         PrimeFaces.current().executeScript("errorSquare('square changeRankErr', true)");
         return;
        }
        
        if (UserSQLUtil.teamNameInGroup(u.getWorkGroup(), changeTeam_Name))
        {
            Team cur = UserSQLUtil.getTeamByNameInGroup(u.getWorkGroup(), changeTeam_Name);
            u.setTeam(cur);
            u.setRank(r);
            DBManager.saveInDB(u, MainBean.getEm());
            PrimeFaces.current().executeScript("closeOverlayMemberJS()");
        }
        else if(changeTeam_Name.isEmpty())
        {
            u.setTeam(null);
            u.setRank(r);
            DBManager.saveInDB(u, MainBean.getEm());
            PrimeFaces.current().executeScript("closeOverlayMemberJS()");
        }
    }

    public void solvedTask(Task t)
    {
        User solver = t.getUser();
        solver.setScore(solver.getScore()+t.getScore());
        deleteTask(t);
        PrimeFaces.current().executeScript("closeOverlay()");
    }
    
    public void createProject()
    {
        clearProjectCreateSquares();
        if (validateProject())
        {
            while (UserSQLUtil.projectNameIsInGroup(createP_Name, currentUser.getWorkGroup()))
            {
                createP_Name += "_new";
            }
            Project p = EntityFactory.createProjectObject(createP_Name, createP_Description, java.sql.Date.valueOf(LocalDate.now()), DateConverter.utilDateToSqlDate(createP_DeadlineDate), currentUser, MainBean.getEm());
            EntityFactory.createWorkAllocationObject(currentUser.getWorkGroup(), currentUser, p, MainBean.getEm());
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Erfolgreich", "Projekt erstellt.\nJetzt unter Projekt einrichten."));
            PrimeFaces.current().executeScript("projectResetter()");

        }
        else
        {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Fehlgeschlagen", "Projekt konnte nicht erstellt werden.\nBitte überprüfen Sie ihre Eingabe."));
        }
    }

    private boolean validateProject()
    {
        List<String> falseSquares = new ArrayList<>();
        if (createP_Name.isEmpty())
        {
            falseSquares.add("square crePNameErr");
        }
        if (createP_Description.isEmpty())
        {
            falseSquares.add("square crePDescriptionErr");
        }
        if (createP_DeadlineDate == null || createP_DeadlineDate.before(DateConverter.utilDateToSqlDate(java.sql.Date.valueOf(LocalDate.now()))))
        {
            falseSquares.add("square crePDeadlineErr");
        }

        if (!falseSquares.isEmpty())
        {
            for (String s : falseSquares)
            {
                PrimeFaces.current().executeScript("errorSquare('" + s + "', true)");
            }
            return false;
        }

        return true;
    }

    public void sendGroupJoinRequest()
    {        
        if (join_GroupName != null && !join_GroupName.isEmpty())
        {
            if (UserSQLUtil.workGroupNameTaken(join_GroupName) && !UserSQLUtil.userIsJoining(currentUser))
            {
                System.out.println(join_GroupName);
                WorkGroup wg = UserSQLUtil.getWorkGroupWithName(join_GroupName);
                EntityFactory.createGroupJoinRequestObject(wg, currentUser, MainBean.getEm());
                
            }
        }
    }

    public void deleteTask(Task t)
    {
        DBManager.deleteFromDB(t, MainBean.getEm());
    }

    public void createGroup()
    {
        if (!UserSQLUtil.workGroupNameTaken(create_groupname))
        {
            if(create_groupname.length() > 13)
            {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Fehlgeschlagen", "Gruppenname ist zu lang."));
                return;
            }
            SmtpConfig s = EntityFactory.createSmtpConfigObject("fakesmtp", currentUser.getEmail(), MainBean.getEm());
            WorkGroup w = EntityFactory.createWorkGroupObject(create_groupname, s, MainBean.getEm());
            currentUser.setWorkGroup(w);
            currentUser.setRank(Rank.Admin);
            DBManager.saveInDB(currentUser, MainBean.getEm());
            FacesContext context = FacesContext.getCurrentInstance();
            context.getApplication().evaluateExpressionGet(context, "#{userBean}", UserBean.class).setCurTab("Projectscreen");
            context.addMessage(null, new FacesMessage("Gruppe erstellt", create_groupname + " wurde erstellt"));
        }
        else
        {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Fehlgeschlagen", "Gruppenname bereits vergeben."));
        }

    }

    public void changeGroupName()
    {
        if (currentUser.getRank() == Rank.Admin)
        {
            WorkGroup wg = currentUser.getWorkGroup();
            wg.setName(change_name);
            currentUser.setWorkGroup(wg);
            DBManager.saveInDB(wg, MainBean.getEm());
            DBManager.saveInDB(currentUser, MainBean.getEm());
        }
    }

    public void leaveGroup()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Gruppe verlassen", "Sie haben " + currentUser.getWorkGroup().getName() + " verlassen."));
        if(UserSQLUtil.getAllUsersFromGroup(currentUser.getWorkGroup()).size() == 1)
        {
            UserSQLUtil.deleteGroupInDB(currentUser.getWorkGroup());
        }
        currentUser.setRank(Rank.Arbeiter);
        currentUser.setScore(0);
        currentUser.setTeam(null);
        currentUser.setWorkGroup(null);
        DBManager.saveInDB(currentUser, MainBean.getEm());
        context.getApplication().evaluateExpressionGet(context, "#{userBean}", UserBean.class).setCurTab("Welcomescreen");
    }

    public void checkUserlogin()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        currentUser = context.getApplication().evaluateExpressionGet(context, "#{userBean}", UserBean.class).getCurrentUser();
        if (currentUser == null)
        {
            try
            {
                FacesContext.getCurrentInstance().getExternalContext().redirect("./../../");//Wildfly
            }
            catch (IOException ex)
            {
                Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            change_name = currentUser.getName();
            change_lastname = currentUser.getLastName();
            change_email = currentUser.getEmail();
            if (currentUser.getWorkGroup() != null)
            {
                change_groupname = currentUser.getWorkGroup().getName();
                smtp_Host = currentUser.getWorkGroup().getSmtpConfig().getSmtpHost();
                smtp_Mail = currentUser.getWorkGroup().getSmtpConfig().getSenderMail();
            }
            else
            {
                context.getApplication().evaluateExpressionGet(context, "#{userBean}", UserBean.class).setCurTab("Welcomescreen");
            }
        }
    }

    public void changePassword()
    {
        if (validPasswordInput())
        {
            currentUser.setPassword(Hashcode.encrypt(pw_newPassword));
            DBManager.saveInDB(currentUser, MainBean.getEm());

            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Erfolgreich", "Passwort gespeichert."));
            pw_password = null;
            pw_newPassword = null;
            pw_newVerifyPassword = null;
        }
    }

    private boolean validPasswordInput()
    {
        List<String> falseSquares = new ArrayList<>();

        //new password
        if (!InputValidation.validatePassword(pw_newPassword) || pw_newPassword.isEmpty())
        {
            falseSquares.add("square pwNewPasswordErr");
        }
        if (!InputValidation.validatePassword(pw_newVerifyPassword) || pw_newVerifyPassword.isEmpty())
        {
            falseSquares.add("square pwNewVerifyPasswordErr");
        }
        if (!pw_newVerifyPassword.equals(pw_newPassword))
        {
            falseSquares.add("square pwNewVerifyPasswordErr");
        }

        //normal password
        if (!InputValidation.validatePassword(pw_password) || pw_password.isEmpty())
        {
            falseSquares.add("square pwPasswordErr");
        }

        if (!falseSquares.isEmpty())
        {
            for (String s : falseSquares)
            {
                PrimeFaces.current().executeScript("errorSquare('" + s + "', true)");
            }
            return false;
        }

        return true;
    }

    public void changeSettings()
    {
        if (validSettingsInput())
        {
            currentUser.setName(change_name);
            currentUser.setLastName(change_lastname);
            currentUser.setEmail(change_email);
            DBManager.saveInDB(currentUser, MainBean.getEm());

            change_name = currentUser.getName();
            change_lastname = currentUser.getLastName();
            change_email = currentUser.getEmail();
            change_password = null;
            change_verifyPassword = null;
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Erfolgreich", "Daten gespeichert."));
        }
    }

    private boolean validSettingsInput()
    {
        List<String> falseSquares = new ArrayList<>();
        if (!InputValidation.validateName(change_name) || change_name.isEmpty())
        {
            falseSquares.add("square chNameErr");
        }
        if (!InputValidation.validateName(change_lastname) || change_lastname.isEmpty())
        {
            falseSquares.add("square chLastNameErr");
        }
        if (!change_email.equals(currentUser.getEmail()) || change_email.isEmpty())
        {
            if (!InputValidation.validateEmail(change_email))
            {
                falseSquares.add("square chMailErr");
            }
        }
        if (!InputValidation.validatePassword(change_password) || change_password.isEmpty())
        {
            falseSquares.add("square chPasswordErr");
        }
        if (!InputValidation.validatePassword(change_verifyPassword) || change_verifyPassword.isEmpty())
        {
            falseSquares.add("square chVerifyPasswordErr");
        }
        if (!change_verifyPassword.equals(change_password))
        {
            falseSquares.add("square chVerifyPasswordErr");
        }

        if (!falseSquares.isEmpty())
        {
            for (String s : falseSquares)
            {
                PrimeFaces.current().executeScript("errorSquare('" + s + "', true)");
            }
            return false;
        }

        return true;
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="Getter/Setter">
        public String getChangeTeam_Rank() {
        return changeTeam_Rank;
    }

    public void setChangeTeam_Rank(String changeTeam_Rank) {
        this.changeTeam_Rank = changeTeam_Rank;
    }
    
    public String getChangeTOwn_Status()
    {
        return changeTOwn_Status;
    }

    public void setChangeTOwn_Status(String changeTOwn_Status)
    {
        this.changeTOwn_Status = changeTOwn_Status;
    }

    public String getChangeT_score()
    {
        return changeT_score;
    }

    public void setChangeT_score(String changeT_score)
    {
        this.changeT_score = changeT_score;
    }

    public void createP_DeadlineDateChange(SelectEvent event)
    {
        createP_DeadlineDate = (Date) event.getObject();
    }

    public void createT_DeadlineDateChange(SelectEvent event)
    {
        createT_DeadlineDate = (Date) event.getObject();
    }

    public void changeP_DeadlineDateChange(SelectEvent event)
    {
        this.changeP_DeadlineDate = DateConverter.utilDateToSqlDate((Date) event.getObject());
    }

    public void changeT_DeadlineDateChange(SelectEvent event)
    {
        this.changeT_DeadlineDate = DateConverter.utilDateToSqlDate((Date) event.getObject());
    }

    public String getSmtp_Host()
    {
        return smtp_Host;
    }

    public void setSmtp_Host(String smtp_Host)
    {
        this.smtp_Host = smtp_Host;
    }

    public String getSmtp_Mail()
    {
        return smtp_Mail;
    }

    public void setSmtp_Mail(String smtp_Mail)
    {
        this.smtp_Mail = smtp_Mail;
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(User currentUser)
    {
        this.currentUser = currentUser;
    }

    public String getJoin_GroupName()
    {
        return join_GroupName;
    }

    public void setJoin_GroupName(String join_GroupName)
    {
        this.join_GroupName = join_GroupName;
    }

    public String getCreateT_Name()
    {
        return createT_Name;
    }

    public void setCreateT_Name(String createT_Name)
    {
        this.createT_Name = createT_Name;
    }

    public String getCreateT_Description()
    {
        return createT_Description;
    }

    public void setCreateT_Description(String createT_Description)
    {
        this.createT_Description = createT_Description;
    }

    public Date getCreateT_DeadlineDate()
    {
        return createT_DeadlineDate;
    }

    public void setCreateT_DeadlineDate(Date createT_DeadlineDate)
    {
        this.createT_DeadlineDate = createT_DeadlineDate;
    }

    public String getCreateT_ProjectName()
    {
        return createT_ProjectName;
    }

    public void setCreateT_ProjectName(String createT_ProjectName)
    {
        this.createT_ProjectName = createT_ProjectName;
    }

    public String getCreateT_Score()
    {
        return createT_Score;
    }

    public void setCreateT_Score(String createT_Score)
    {
        this.createT_Score = createT_Score;
    }

    public String getCreateT_Usermail()
    {
        return createT_Usermail;
    }

    public void setCreateT_Usermail(String createT_Usermail)
    {
        this.createT_Usermail = createT_Usermail;
    }

    public String getCreateP_Name()
    {
        return createP_Name;
    }

    public void setCreateP_Name(String createP_Name)
    {
        this.createP_Name = createP_Name;
    }

    public String getCreateP_Description()
    {
        return createP_Description;
    }

    public void setCreateP_Description(String createP_Description)
    {
        this.createP_Description = createP_Description;
    }

    public Date getCreateP_DeadlineDate()
    {
        return createP_DeadlineDate;
    }

    public void setCreateP_DeadlineDate(Date createP_DeadlineDate)
    {
        this.createP_DeadlineDate = createP_DeadlineDate;
    }

    public String getChange_groupname()
    {
        return change_groupname;
    }

    public void setChange_groupname(String change_groupname)
    {
        this.change_groupname = change_groupname;
    }

    public String getChange_name()
    {
        return change_name;
    }

    public void setChange_name(String change_name)
    {
        this.change_name = change_name;
    }

    public String getChange_lastname()
    {
        return change_lastname;
    }

    public void setChange_lastname(String change_lastname)
    {
        this.change_lastname = change_lastname;
    }

    public String getChange_email()
    {
        return change_email;
    }

    public void setChange_email(String change_email)
    {
        this.change_email = change_email;
    }

    public String getChange_password()
    {
        return change_password;
    }

    public void setChange_password(String change_password)
    {
        this.change_password = change_password;
    }

    public String getChange_verifyPassword()
    {
        return change_verifyPassword;
    }

    public void setChange_verifyPassword(String change_verifyPassword)
    {
        this.change_verifyPassword = change_verifyPassword;
    }

    public String getChange_newPassword()
    {
        return pw_newPassword;
    }

    public void setChange_newPassword(String change_newPassword)
    {
        this.pw_newPassword = change_newPassword;
    }

    public String getChange_newVerifyPassword()
    {
        return pw_newVerifyPassword;
    }

    public void setChange_newVerifyPassword(String change_newVerifyPassword)
    {
        this.pw_newVerifyPassword = change_newVerifyPassword;
    }

    public String getPw_password()
    {
        return pw_password;
    }

    public void setPw_password(String pw_password)
    {
        this.pw_password = pw_password;
    }

    public String getPw_newPassword()
    {
        return pw_newPassword;
    }

    public void setPw_newPassword(String pw_newPassword)
    {
        this.pw_newPassword = pw_newPassword;
    }

    public String getPw_newVerifyPassword()
    {
        return pw_newVerifyPassword;
    }

    public void setPw_newVerifyPassword(String pw_newVerifyPassword)
    {
        this.pw_newVerifyPassword = pw_newVerifyPassword;
    }

    public String getCreate_groupname()
    {
        return create_groupname;
    }

    public void setCreate_groupname(String create_groupname)
    {
        this.create_groupname = create_groupname;
    }

    public String getCreateTeam_Name()
    {
        return createTeam_Name;
    }

    public void setCreateTeam_Name(String createTeam_Name)
    {
        this.createTeam_Name = createTeam_Name;
    }

    public String getChangeTeam_Name()
    {
        return changeTeam_Name;
    }

    public void setChangeTeam_Name(String changeTeam_Name)
    {
        this.changeTeam_Name = changeTeam_Name;
    }

    public String getChangeT_ProjectName()
    {
        return changeT_ProjectName;
    }

    public void setChangeT_ProjectName(String changeT_ProjectName)
    {
        this.changeT_ProjectName = changeT_ProjectName;
    }

    public String getChangeT_UserMail()
    {
        return changeT_UserMail;
    }

    public void setChangeT_UserMail(String changeT_UserMail)
    {
        this.changeT_UserMail = changeT_UserMail;
    }

    public String getChangeT_Status()
    {
        return changeT_Status;
    }

    public void setChangeT_Status(String changeT_Status)
    {
        this.changeT_Status = changeT_Status;
    }

    //</editor-fold>
}
