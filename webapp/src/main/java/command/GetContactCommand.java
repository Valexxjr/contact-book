package command;

import DAO.Impl.DAOContactImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetContactCommand implements Command {

    private static Logger logger = LogManager.getLogger(GetContactCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();
        Integer id;
        try {
            id = Integer.parseInt(uri.replace("/ContactBook/contacts/", ""));
        } catch (NumberFormatException e) {
            logger.error("error while getting contact: invalid contact id ");
            res.sendError(400);
            return;
        }

        PrintWriter out = res.getWriter();
        String json = new ObjectMapper().writeValueAsString(new DAOContactImpl().get(id));
        out.println(json);
    }
}
