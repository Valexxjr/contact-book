package command;

import DAO.Impl.DAOAttachmentImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetContactAttachmentsCommand implements Command{

    private static Logger logger = LogManager.getLogger(GetContactAttachmentsCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        String[] uriParts = req.getRequestURI().split("/");
        Integer contactId;
        try {
            contactId = Integer.parseInt(uriParts[3]);
        } catch (NumberFormatException e) {
            logger.error("invalid contact id " + uriParts[3]);
            res.sendError(400);
            return;
        }

        PrintWriter out = res.getWriter();
        String json = new ObjectMapper().writeValueAsString(new DAOAttachmentImpl().getAllByContact(contactId));
        out.println(json);
        logger.info("contact : id = " + uriParts[3] + " attachments get");
    }
}
