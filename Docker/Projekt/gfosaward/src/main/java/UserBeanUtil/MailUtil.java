/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserBeanUtil;

import Tables.Task;
import Tables.User;
import Tables.WorkGroup;
import Util.Rank;
import Util.TaskStatus;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Henry
 */
public class MailUtil
{
    public static boolean sendTaskStatusChangeMail(WorkGroup wg, Task task)//Email to notify the creator of a task that its status has been changed
    {
        if(!wg.getSmtpConfig().getSenderMail().isEmpty())
        {
            User reciver = task.getCreator();
            String recieverMail = reciver.getEmail();
            String senderMail = wg.getSmtpConfig().getSenderMail();
            String smtpHost = wg.getSmtpConfig().getSmtpHost();
            String subject = "Aufgabenstatus Änderung";
            String content = "Am "+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +" hat sich der Status der Aufgabe '" + task.getName() + "' geändert.\n"+
                    "Der neue Status ist " + task.getTaskStatus() + ".\n";
            if(task.getTaskStatus() == TaskStatus.Fertig)
            {
                content += "Sie können die Aufgabe als Ordnungsgemäß beendet unter dem Reiter Projekt löschen.\n"+
                        "So werden " + task.getUser().getName() + " " + task.getUser().getLastName() + " " + task.getScore() + " Punkte auf seinem Konto gut geschrieben.\n";
            }
            try
            {
                sendMail(subject, content, senderMail, recieverMail, smtpHost);
                System.out.println("-------------------------------Mail was send!-----------------------------");
                return true;
            }catch (Exception e)
            {
                System.out.println("------------------------Failed to send mail!---------------------------");
                return false;
            }
        }
        return false;
    }
    public static boolean sendTaskAssignmentMail(Task task, User reciver, User creator, WorkGroup wg)//Email to notify a user that he got a task
    {
        if(!wg.getSmtpConfig().getSenderMail().isEmpty())
        {
            String recieverMail = reciver.getEmail();
            String senderMail = wg.getSmtpConfig().getSenderMail();
            String smtpHost = wg.getSmtpConfig().getSmtpHost();
            String subject = "Neue Aufgabe";
            String content = "Am "+ LocalDate.now() +" wurde Ihnen die Aufgabe '" + task.getName() + "' zugewiesen\n" +
                    task.getName() + ":\n" +
                    task.getDescription() + "\n" +
                    "Muss am " + task.getDeadlineDate().toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " fertiggestellt werden. \n\n" +
                    "Diese aufgabe wurde von " + creator.getName() + " " + creator.getLastName() + " zugewiesen.";
            try
            {
                sendMail(subject, content, senderMail, recieverMail, smtpHost);
                System.out.println("-------------------------------Mail was send!-----------------------------");
                return true;
            }catch (Exception e)
            {
                System.out.println("------------------------Failed to send mail!---------------------------");
                return false;
            }
        }
        return false;
    }
    public static void sendMail(String subject, String content, String sender, String reciever, String smtpHost) throws MessagingException//actual sending of mail
    {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", smtpHost);
        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(reciever));
        message.setSubject(subject, "ISO-8859-1");
        message.setText(content);
        Transport.send(message);
    }
}
