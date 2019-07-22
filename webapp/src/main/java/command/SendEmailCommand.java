package command;

import DAO.Impl.DAOContactImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.SendEmail;
import model.Contact;
import model.EmailDetails;
import org.antlr.stringtemplate.StringTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SendEmailCommand implements Command {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");

        EmailDetails emailDetails = new ObjectMapper().readValue(req.getInputStream(), EmailDetails.class);
        DAOContactImpl daoContact = new DAOContactImpl();
        for(String idString: emailDetails.getReceivers().split(",")) {
            Integer id = Integer.parseInt(idString);
            Contact contact = daoContact.get(id);
            if(!SendEmail.sendEmail("contactbookmail@gmail.com", contact,
                    emailDetails.getTitle(), new StringTemplate(emailDetails.getText()))) {
                res.sendError(400);
                return;
            }
        }
    }
}
