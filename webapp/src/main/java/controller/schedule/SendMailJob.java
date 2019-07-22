package controller.schedule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DAO.DAOContact;
import DAO.Impl.DAOContactImpl;
import controller.SendEmail;
import dto.ContactWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

public class SendMailJob implements Job {

    private static Logger logger = LogManager.getLogger(SendMailJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("mail sending job started");

        DAOContact daoContact = new DAOContactImpl();

        List<ContactWrapper> allContacts = daoContact.getAll();

        LocalDate today  = LocalDate.now();

        List<ContactWrapper> contactsBirthday = new ArrayList<>();

        StringBuilder mailText = new StringBuilder("Contacts with birthday today: ");

        allContacts.forEach(contactWrapper -> {
            LocalDate birthdayDate = contactWrapper.getBirthdayDate();
            if(birthdayDate != null)
                if(birthdayDate.getDayOfMonth() == today.getDayOfMonth() &&
                        birthdayDate.getMonth() == today.getMonth()) {
                    mailText.append(contactWrapper.getFirstName() + " " + contactWrapper.getLastName() + "\n");
                }
        });

        SendEmail.sendEmailToAdmin("contactbookvalay@gmail.com", "Birthdays", mailText.toString());

        logger.info("birthdays mail sent");
    }
}