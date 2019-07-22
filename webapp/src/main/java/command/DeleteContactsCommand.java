package command;

import DAO.DAOContact;
import DAO.Impl.DAOContactImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DeleteContactsCommand implements Command {

    private static Logger logger = LogManager.getLogger(DeleteContactsCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Integer[] ids = objectMapper.readValue(req.getInputStream(), Integer[].class);
        DAOContact daoContact = new DAOContactImpl();
        if(daoContact.deleteContacts(ids)) {
            logger.info("contacts with ids " + ids + " deleted");
            res.setStatus(204);
        }
        else {
            logger.error("error while deleting contacts, ids" + ids);
            res.setStatus(404);
        }
    }
}
