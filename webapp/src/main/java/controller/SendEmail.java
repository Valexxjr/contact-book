package controller;

import model.Contact;
import org.antlr.stringtemplate.StringTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail {

    private static Logger logger = LogManager.getLogger(SendEmail.class);

    final static String username = "contactbookvalay@gmail.com";
    final static String password = "contactbook1";

    final static Properties properties = new Properties();

    static {
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    }

    public static boolean sendEmail(String sender, Contact contact, String title, StringTemplate messageText) {
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(contact.getEmail())
            );

            message.setSubject(title, "UTF-8");

            messageText.setAttribute("firstName", contact.getFirstName());
            messageText.setAttribute("lastName", contact.getLastName());
            messageText.setAttribute("patronymic", contact.getPatronymic());
            messageText.setAttribute("placeOfWork", contact.getPlaceOfWork());
            messageText.setAttribute("maritalStatus", contact.getMaritalStatus());
            messageText.setAttribute("citizenship", contact.getCitizenship());
            messageText.setAttribute("birthdayDate", contact.getBirthdayDate());
            messageText.setAttribute("gender", contact.getGender());
            messageText.setAttribute("website", contact.getWebsite());

            message.setText(messageText.toString(), "UTF-8");

            Transport.send(message);
            logger.info("Sent messages successfully");
            return true;
        } catch (MessagingException mex) {
            logger.error("error while sending messages");
            return false;
        }
    }

    public static void sendEmailToAdmin(String sender, String title, String messageText) {
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(username)
            );

            message.setSubject(title);
            message.setText(messageText);

            Transport.send(message);
            logger.info("Sent message successfully");
        } catch (MessagingException mex) {
            logger.error("error while sending message");
        }
    }

}